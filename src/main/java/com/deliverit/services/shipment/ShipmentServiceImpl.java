package com.deliverit.services.shipment;

import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.deliverit.Utils.throwIfNotEmployee;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    public static final String LIST_ERROR_MESSAGE = "Only employees can list shipments.";
    public static final String CREATE_ERROR_MESSAGE = "Only employees can create shipments.";
    public static final String UPDATE_ERROR_MESSAGE = "Only employees can update shipments.";
    public static final String PARCEL_ERROR_MESSAGE = "Only employees can add or remove parcels.";

    private final ShipmentRepository shipmentRepository;
    private final ParcelRepository parcelRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ParcelRepository parcelRepository) {
        this.shipmentRepository = shipmentRepository;
        this.parcelRepository = parcelRepository;
    }

    @Override
    public List<Shipment> getAll(User user) {
        throwIfNotEmployee(user, LIST_ERROR_MESSAGE);
        return shipmentRepository.getAll();
    }

    @Override
    public List<Shipment> getShipmentsOnTheWay(User user) {
        throwIfNotEmployee(user, LIST_ERROR_MESSAGE);
        return shipmentRepository.getShipmentsOnTheWay(user);
    }

    @Override
    public Shipment getById(int id) {
        return shipmentRepository.getById(id);
    }

    @Override
    public void create(User user, Shipment shipment) {
        throwIfNotEmployee(user, CREATE_ERROR_MESSAGE);
        shipmentRepository.create(shipment);
    }

    @Override
    public void update(User user, Shipment shipment) {
        throwIfNotEmployee(user, UPDATE_ERROR_MESSAGE);
        shipmentRepository.update(shipment);
    }

    @Override
    public void delete(int id) {
        shipmentRepository.delete(id);
    }

    @Override
    public List<Shipment> filter(User user, Optional<Integer> originWarehouseId, Optional<Integer> destinationWarehouseId) {
        throwIfNotEmployee(user, UPDATE_ERROR_MESSAGE);
        return shipmentRepository.filter(originWarehouseId, destinationWarehouseId);
    }
    @Override
    public List<Shipment> filterByCustomer(User user, int customerId) {
        throwIfNotEmployee(user, UPDATE_ERROR_MESSAGE);
        return shipmentRepository.filterByCustomer(customerId);
    }
    @Override
    public void addParcel(User user, int shipmentId, int parcelId) {
        throwIfNotEmployee(user, PARCEL_ERROR_MESSAGE);
        Parcel parcel = parcelRepository.getById(parcelId);
        shipmentRepository.addParcel(shipmentId, parcel);
    }

    @Override
    public void removeParcel(User user, int shipmentId, int parcelId) {
        throwIfNotEmployee(user, PARCEL_ERROR_MESSAGE);
        Parcel parcel = parcelRepository.getById(parcelId);
        shipmentRepository.removeParcel(shipmentId, parcel);
    }

    @Override
    public String checkShipmentStatus(int shipmentId) {
        return shipmentRepository.checkShipmentStatus(shipmentId).toString();
    }

    @Override
    public List<Shipment> filterShipmentsByWarehouse(Integer originWarehouseId, Integer destinationWarehouseId, Integer customerId) {
        return shipmentRepository.filterShipmentsByWarehouse(originWarehouseId, destinationWarehouseId, customerId);
    }

}
