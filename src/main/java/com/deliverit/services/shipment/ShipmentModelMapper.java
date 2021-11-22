package com.deliverit.services.shipment;

import com.deliverit.models.*;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentModelMapper {

    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final ParcelRepository parcelRepository;

    @Autowired
    public ShipmentModelMapper(ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository, ParcelRepository parcelRepository) {
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.parcelRepository = parcelRepository;
    }

    public Shipment fromDto(ShipmentDto shipmentDto) {
        Shipment shipment = new Shipment();
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    public Shipment fromDto(ShipmentDto shipmentDto, int id) {
        Shipment shipment = shipmentRepository.getById(id);
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    private void dtoToObject(ShipmentDto shipmentDto, Shipment shipment) {
        Warehouse originWarehouse = warehouseRepository.getById(shipmentDto.getOriginWarehouseId());
        Warehouse destinationWarehouse = warehouseRepository.getById(shipmentDto.getDestinationWarehouseId());
        shipment.setArrivalDate(shipmentDto.getArrivalDate());
        shipment.setDepartureDate(shipmentDto.getDepartureDate());
        shipment.setOriginWarehouse(originWarehouse);
        shipment.setDestinationWarehouse(destinationWarehouse);
        shipment.setStatus(shipmentDto.getStatus());
    }

    public ShipmentDto toDto(Shipment shipment) {
        ShipmentDto shipmentDto = new ShipmentDto();
        shipmentDto.setParcels(shipment.getParcels());
        shipmentDto.setArrivalDate(shipment.getArrivalDate());
        shipmentDto.setDepartureDate(shipment.getDepartureDate());
        shipmentDto.setStatus(shipment.getStatus());
        shipmentDto.setDestinationWarehouseId(shipment.getDestinationWarehouse().getId());
        shipmentDto.setOriginWarehouseId(shipment.getOriginWarehouse().getId());
        return shipmentDto;
    }
}
