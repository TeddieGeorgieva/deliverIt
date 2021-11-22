package com.deliverit.services.parcel;

import com.deliverit.models.Parcel;
import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ParcelService {

    List<Parcel> getAll(User user);

    List<Parcel> getAllForUser(User user);

    Parcel getById(int id);

    void create(Parcel parcel, User user);

    void update(Parcel parcel, User user);

    void changeDeliveryType(Parcel parcel);

    void delete(int id, User user);

    List<Parcel> filter(User user, Optional<Double> weight, Optional<Integer> customerId,
                        Optional<Integer> warehouseId, Optional<Integer> categoryId,
                        Optional<String> sort);

    List<Parcel> search(Optional<String> search);

    String checkShipmentStatus(User user, int parcelId);

    int findShipment(int parcelId);

    List<Parcel> filterParcels(User user, Integer customerId, Integer categoryId, Double weight, Integer destinationWarehouseId, String searchAll);
}
