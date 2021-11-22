package com.deliverit.repositories.shipment;

import com.deliverit.models.*;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository {

    List<Shipment> getAll();

    List<Shipment> getShipmentsOnTheWay(User user);

    Shipment getById(int id);

    void create(Shipment shipment);

    void update(Shipment shipment);

    void delete(int id);

    List<Shipment> filter(Optional<Integer> originWarehouseId, Optional<Integer> destinationWarehouseId);

    List<Shipment> filterByCustomer(int customerId);

    void addParcel(int shipmentId, Parcel parcel);

    void removeParcel(int shipmentId, Parcel parcel);

    Status checkShipmentStatus(int shipmentId);

    List<Shipment> filterShipmentsByWarehouse(Integer originWarehouseId, Integer destinationWarehouseId, Integer customerId);
}
