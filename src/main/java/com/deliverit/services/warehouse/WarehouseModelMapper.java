package com.deliverit.services.warehouse;

import com.deliverit.models.*;
import com.deliverit.repositories.address.AddressRepository;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseModelMapper {
    private final WarehouseRepository warehouseRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public WarehouseModelMapper(WarehouseRepository warehouseRepository, AddressRepository addressRepository) {
        this.warehouseRepository = warehouseRepository;
        this.addressRepository = addressRepository;
    }

    public Warehouse fromDto(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        dtoToObject(warehouseDto, warehouse);
        return warehouse;
    }

    public Warehouse fromDto(WarehouseDto warehouseDto, int id) {
        Warehouse warehouse = warehouseRepository.getById(id);
        dtoToObject(warehouseDto, warehouse);
        return warehouse;
    }

    private void dtoToObject(WarehouseDto warehouseDto, Warehouse warehouse) {
        warehouse.setAddress(addressRepository.getById(warehouseDto.getAddressId()));
    }

    public WarehouseDto toDto(Warehouse warehouse) {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setAddressId(warehouse.getAddress().getId());
        return warehouseDto;
    }
}
