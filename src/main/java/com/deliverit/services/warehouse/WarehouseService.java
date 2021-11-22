package com.deliverit.services.warehouse;

import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.Warehouse;
import com.deliverit.models.WarehouseDto;

import java.util.List;

public interface WarehouseService {

    List<Warehouse> getAll();

    Warehouse getById(int id);

    Shipment getNextArrivingShipment(int id, User user);

    void create(User user, Warehouse warehouse);

    void update(Warehouse warehouse, User user);

    void delete(int id, User user);

}
