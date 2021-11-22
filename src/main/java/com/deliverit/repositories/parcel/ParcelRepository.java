package com.deliverit.repositories.parcel;

import com.deliverit.models.Parcel;
import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository {

    List<Parcel> getAll();

    List<Parcel> getAllForUser(int userId);

    Parcel getById(int id);

    void create(Parcel parcel);

    void update(Parcel parcel);

    void delete(int id);

    List<Parcel> filter(Optional<Double> weight, Optional<Integer> customerId,
                        Optional<Integer> warehouseId, Optional<Integer> categoryId,
                        Optional<String> sort);

    List<Parcel> search(String search);

    void changeDeliveryType(Parcel parcel, int shipmentId);

    List<Parcel> filterParcels(Integer customerId, Integer categoryId, Double weight, Integer destinationWarehouseId, String searchAll);
}
