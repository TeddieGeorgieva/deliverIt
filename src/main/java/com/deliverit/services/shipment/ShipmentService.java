package com.deliverit.services.shipment;

import com.deliverit.models.Shipment;
import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    List<Shipment> getAll(User user);

    List<Shipment> getShipmentsOnTheWay(User user);

    Shipment getById(int id);

    void create(User user, Shipment shipment);

    void update(User user, Shipment shipment);

    void delete(int id);

    List<Shipment> filter(User user,Optional<Integer> originWarehouseId, Optional<Integer> destinationWarehouseId);

    List<Shipment> filterByCustomer(User user, int customerId);

    void addParcel(User user, int shipmentId, int parcelId);

    void removeParcel(User user, int shipmentId, int parcelId);

    String checkShipmentStatus(int shipmentId);

    List<Shipment> filterShipmentsByWarehouse(Integer originWarehouseId, Integer destinationWarehouseId, Integer customerId);
}
