package com.deliverit.services.parcel;

import com.deliverit.models.*;
import com.deliverit.repositories.category.CategoryRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import com.deliverit.repositories.users.UserRepository;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParcelModelMapper {
    private final ParcelRepository parcelRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final CategoryRepository categoryRepository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ParcelModelMapper(ParcelRepository parcelRepository, UserRepository userRepository, WarehouseRepository warehouseRepository, CategoryRepository categoryRepository, ShipmentRepository shipmentRepository) {
        this.parcelRepository = parcelRepository;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
        this.shipmentRepository = shipmentRepository;
    }

    public Parcel fromDto(ParcelDto parcelDto) {
        Parcel parcel = new Parcel();
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    public Parcel fromDto(ParcelDto parcelDto, int id) {
        Parcel parcel = parcelRepository.getById(id);
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    private void dtoToObject(ParcelDto parcelDto, Parcel parcel) {
        User user = userRepository.getByUsername((parcelDto.getPurchaserUsername()));
        Warehouse warehouse = warehouseRepository.getById(parcelDto.getDestinationWarehouseId());
        Category category = categoryRepository.getById(parcelDto.getCategoryId());
        parcel.setPurchaser(user);
        parcel.setDestinationWarehouse(warehouse);
        parcel.setCategory(category);
        parcel.setWeight(parcelDto.getWeight());
        parcel.setDeliveryType(parcelDto.getDeliveryType());


        if (parcelDto.getShipmentId() != -1) {
            Shipment shipmentToAdd = shipmentRepository.getById(parcelDto.getShipmentId());
            if (!shipmentToAdd.getParcels().contains(parcel)) {
                List<Shipment> shipments = new ArrayList<>();
                shipments.add(shipmentToAdd);
                parcel.setShipments(shipments);
                shipmentRepository.addParcel(parcelDto.getShipmentId(), parcel);
            }

        }
    }

    public ParcelDto toDto(Parcel parcel) {
        ParcelDto parcelDto = new ParcelDto();
        parcelDto.setWeight(parcel.getWeight());
        parcelDto.setCategoryId(parcel.getCategory().getId());
        parcelDto.setPurchaserUsername(parcel.getPurchaser().getUsername());
        parcelDto.setDestinationWarehouseId(parcel.getDestinationWarehouse().getId());
        parcelDto.setDeliveryType(parcel.getDeliveryType());

        if (parcel.getShipments().isEmpty()) {
            parcelDto.setShipmentId(-1);
        } else {
            parcelDto.setShipmentId(parcel.getShipments().get(0).getId());
        }
        return parcelDto;
    }


//    public Parcel fromDto(ParcelCustomerDto parcelDto, int id) {
//        Parcel parcel = parcelRepository.getById(id);
//        dtoToObject(parcelDto, parcel);
//        return parcel;
//    }
//    private void dtoToObject(ParcelCustomerDto parcelDto, Parcel parcel) {
//        parcel.setDeliveryType(ParcelDeliveryType.values()[parcelDto.getDeliveryType()]);
//    }
//
//    public ParcelCustomerDto toDtoForCustomer(Parcel parcel) {
//        ParcelCustomerDto dto = new ParcelCustomerDto();
//        dto.setDeliveryType(parcel.getDeliveryType().ordinal());
//        return dto;
//    }

}
