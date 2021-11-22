package com.deliverit.repositories.parcel;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.Status;
import com.deliverit.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ParcelRepositoryImpl implements ParcelRepository {

    private final SessionFactory sessionFactory;
    private final String CHANGE_DELIVERY_ERROR = "You cannot change delivery type. Shipment already \"On the way\"";
    private final String CHANGE_DELIVERY_USER_ERROR = "You can only change Delivery type!";

    @Autowired
    public ParcelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Parcel> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel", Parcel.class);
            return query.list();
        }
    }

    @Override
    public List<Parcel> getAllForUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel where purchaser.id = :userId", Parcel.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public Parcel getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Parcel parcel = session.get(Parcel.class, id);
            if (parcel == null) {
                throw new EntityNotFoundException("Parcel", id);
            }
            return parcel;
        }
    }

    @Override
    public void create(Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            session.save(parcel);
        }
    }

    @Override
    public void update(Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(parcel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Parcel parcelToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(parcelToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight, Optional<Integer> userId,
                               Optional<Integer> warehouseId, Optional<Integer> categoryId,
                               Optional<String> sort) {
        try (Session session = sessionFactory.openSession()) {
            var queryString = new StringBuilder(" from Parcel ");
            var filters = new ArrayList<String>();
            var params = new HashMap<String, Object>();

            weight.ifPresent(value -> {
                filters.add(" weight = :weight ");
                params.put("weight", value);
            });

            userId.ifPresent(value -> {
                filters.add(" purchaser.id = :userId ");
                params.put("userId", value);
            });

            warehouseId.ifPresent(value -> {
                filters.add(" destinationWarehouse.id = :warehouseId ");
                params.put("warehouseId", value);
            });

            categoryId.ifPresent(value -> {
                filters.add(" category.id = :categoryId ");
                params.put("categoryId", value);
            });

            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }

            sort.ifPresent(value -> {
                queryString.append(generateSortingString(value));
            });

            Query<Parcel> query = session.createQuery(queryString.toString(), Parcel.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Parcel> search(String search) {
        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery(
                    "from Parcel where" +
                            " (purchaser.firstName like :firstName" +
                            " and purchaser.lastName like :lastName)" +
                            " and destinationWarehouse like :destinationWarehouse" +
                            " or category = :category", Parcel.class
            );
            query.setParameter("firstName", "%" + search + "%");
            query.setParameter("lastName", "%" + search + "%");
            query.setParameter("destinationWarehouse", "%" + search + "%");
            query.setParameter("category", "%" + search + "%");
            return query.list();
        }
    }

    @Override
    public void changeDeliveryType(Parcel parcel, int shipmentId) {
        try (Session session = sessionFactory.openSession()) {

            Shipment shipment = session.get(Shipment.class, shipmentId);
            Parcel parcelToUpdate = shipment.getParcels().stream()
                    .filter(p -> p.getId() == parcel.getId())
                    .findAny()
                    .get();

            if (shipment.getStatus().toString().equals(Status.COMPLETED.toString())) {
                throw new IllegalArgumentException(CHANGE_DELIVERY_ERROR);
            } else if (parcelToUpdate.getDeliveryType() != parcel.getDeliveryType()) {
                update(parcel);
            } else {
                throw new UnauthorizedOperationException(CHANGE_DELIVERY_USER_ERROR);
            }
        }
    }

    @Override
    public List<Parcel> filterParcels(Integer customerId, Integer categoryId, Double weight, Integer destinationWarehouseId, String searchAll) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> filter = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();


            if (customerId != null) {
                filter.add(" user_id = :customerId ");
                params.put("customerId", customerId);
            }
            if (categoryId != null) {
                filter.add(" category_id = :categoryId ");
                params.put("categoryId", categoryId);
            }
            if (weight != null) {
                filter.add(" weight = :weight ");
                params.put("weight", weight);
            }
            if (destinationWarehouseId != null) {
                filter.add(" warehouse_id = :destinationWarehouseId ");
                params.put("destinationWarehouseId", destinationWarehouseId);
            }

            if (customerId == null && categoryId == null && destinationWarehouseId == null && weight == null) {
                stringBuilder.append(" from Parcel ");
            } else {
                stringBuilder.append(" from Parcel where ").append(String.join(" and ", filter));
            }



            Query<Parcel> query = session.createQuery(stringBuilder.toString(), Parcel.class);

            query.setProperties(params);

            return query.list();
        }
    }

    private String generateSortingString(String value) {
        StringBuilder result = new StringBuilder(" order by ");
        var params = value.toLowerCase().split("_");

        switch (params[0]) {
            case "weight":
                result.append("weight ");
                break;
//            case "arrival_date":
//                result.append("Parcel.")
            default:
                return "";
        }

        if (params.length > 1 && params[1].equals("desc")) {
            result.append("desc");
        }
        return result.toString();
    }

    private Double getNumberValueIfPresent(String search) {
        try {
            return Double.parseDouble(search);
        } catch (NumberFormatException exception) {
            return (double) -1;
        }
    }
}
