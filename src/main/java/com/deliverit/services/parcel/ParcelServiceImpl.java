package com.deliverit.services.parcel;

import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.User;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.deliverit.Utils.throwIfNotEmployee;

@Service
public class ParcelServiceImpl implements ParcelService {

    public static final String CREATE_UPDATE_ERROR_MESSAGE = "Only purchaser or admin can create or modify a parcel.";
    public static final String LIST_ERROR_MESSAGE = "You cannot list other users' parcels unless you are an employee!";
    public static final String USER_ERROR_MESSAGE = "You are not authorized to view this parcel status!";
    public static final String ALL_PARCELS_ERROR_MESSAGE = "You are not authorized to see all parcels!";

    private final ParcelRepository parcelRepository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ParcelServiceImpl(ParcelRepository parcelRepository, ShipmentRepository shipmentRepository) {
        this.parcelRepository = parcelRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public List<Parcel> getAll(User user) {
        throwIfNotEmployee(user, LIST_ERROR_MESSAGE);
        if (user.isEmployee()) {
            return parcelRepository.getAll();
        } else {
            throw new UnauthorizedOperationException(ALL_PARCELS_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Parcel> getAllForUser(User user) {
        if (user.isCustomer()) {
            return parcelRepository.getAllForUser(user.getId());
        } else {
            throw new UnauthorizedOperationException(ALL_PARCELS_ERROR_MESSAGE);
        }
    }

    @Override
    public Parcel getById(int id) {
        return parcelRepository.getById(id);
    }

    @Override
    public void create(Parcel parcel, User user) {
        throwIfNotEmployee(user, CREATE_UPDATE_ERROR_MESSAGE);
        parcelRepository.create(parcel);
    }

    @Override
    public void update(Parcel parcel, User user) {
        Parcel toUpdate = parcelRepository.getById(parcel.getId());
        if (user.isEmployee()) {
            parcelRepository.update(parcel);
        } else if (toUpdate.getPurchaser().equals(user) && !toUpdate.getShipments().isEmpty()) {
            parcelRepository.changeDeliveryType(parcel, findShipment(parcel.getId()));
        } else {
            throw new UnauthorizedOperationException(CREATE_UPDATE_ERROR_MESSAGE);
        }

    }

    @Override
    public void changeDeliveryType(Parcel parcel) {
        parcelRepository.changeDeliveryType(parcel, findShipment(parcel.getId()));
    }

    @Override
    public void delete(int id, User user) {
        throwIfNotEmployee(user, CREATE_UPDATE_ERROR_MESSAGE);
        parcelRepository.delete(id);
    }

    @Override
    public List<Parcel> filter(User user, Optional<Double> weight, Optional<Integer> customerId, Optional<Integer> warehouseId,
                               Optional<Integer> categoryId, Optional<String> sort) {
        if (!user.isEmployee()) {
            if (customerId.isEmpty() || customerId.get() != user.getId()) {
                throw new UnauthorizedOperationException(LIST_ERROR_MESSAGE);
            }
        }
        return parcelRepository.filter(weight, customerId, warehouseId, categoryId, sort);
    }

    @Override
    public List<Parcel> search(Optional<String> search) {
        if (search.isEmpty()) {
            return parcelRepository.getAll();
        } else {
            return parcelRepository.search(search.get());
        }
    }


    @Override
    public String checkShipmentStatus(User user, int parcelId) {
        Parcel parcel = parcelRepository.getById(parcelId);
        if (user.getId() != parcel.getPurchaser().getId()) {
            throw new UnauthorizedOperationException(USER_ERROR_MESSAGE);
        }
        return shipmentRepository.checkShipmentStatus(findShipment(parcelId)).toString();
    }

    @Override
    public int findShipment(int parcelId) {
        Parcel parcel = parcelRepository.getById(parcelId);
        int shipmentId = 0;
        for (int i = 0; i < parcel.getShipments().size(); i++) {
            if (parcel.getShipments().get(i).getParcels().contains(parcel)) {
                shipmentId = parcel.getShipments().get(i).getId();
                break;
            }
        }

        return shipmentId;
    }

    @Override
    public List<Parcel> filterParcels(User user, Integer customerId,  Integer categoryId, Double weight, Integer destinationWarehouseId, String searchAll) {
        return parcelRepository.filterParcels(customerId, categoryId, weight, destinationWarehouseId, searchAll);
    }
}
