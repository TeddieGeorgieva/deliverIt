package com.deliverit.repositories.warehouse;

import com.deliverit.models.Address;
import com.deliverit.models.Shipment;
import com.deliverit.models.Warehouse;

import java.util.List;

public interface WarehouseRepository {

    List<Warehouse> getAll();

    Warehouse getById(int id);

    Warehouse getByAddress(Address address);

    Shipment getNextArriving(int id);

    void create(Warehouse warehouse);

    void update(Warehouse warehouse);

    void delete(int id);
}
