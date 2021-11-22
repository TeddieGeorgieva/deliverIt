package com.deliverit.repositories.shipment;

import com.deliverit.exceptions.EntityUpdateException;
import com.deliverit.exceptions.EntityNotFoundException;
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
import java.util.stream.Collectors;

@Repository
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private static final String ADD_PARCEL_ERROR = "The parcel is already in this shipment.";
    private static final String REMOVE_PARCEL_ERROR = "The parcel is not in this shipment.";
    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Shipment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("from Shipment", Shipment.class);
            return query.list();
        }
    }

    @Override
    public List<Shipment> getShipmentsOnTheWay(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("from Shipment where status = 1", Shipment.class);
            return query.list();
        }
    }

    @Override
    public Shipment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Shipment shipment = session.get(Shipment.class, id);
            if (shipment == null) {
                throw new EntityNotFoundException("Shipment", id);
            }
            return shipment;
        }
    }

    @Override
    public void create(Shipment shipment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(shipment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Shipment shipment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(shipment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Shipment shipmentToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(shipmentToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Shipment> filter(Optional<Integer> originWarehouseId, Optional<Integer> destinationWarehouseId) {
        try (Session session = sessionFactory.openSession()) {
            var queryString = new StringBuilder(" from Shipment ");
            var filters = new ArrayList<String>();
            var params = new HashMap<String, Object>();

            originWarehouseId.ifPresent(value -> {
                filters.add(" originWarehouse.id = :originWarehouseId");
                params.put("originWarehouseId", value);
            });
            destinationWarehouseId.ifPresent(value -> {
                filters.add(" destinationWarehouse.id = :destinationWarehouseId ");
                params.put("destinationWarehouseId", value);
            });

            if (!filters.isEmpty()) {
                queryString.append("where ")
                        .append(String.join(" and ", filters));
            }

            Query<Shipment> query = session.createQuery(queryString.toString(), Shipment.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Shipment> filterByCustomer(int userId) {
            List<Shipment> list = getAll();
            return list.stream()
                    .filter(s -> s.getParcels().stream()
                            .anyMatch(p -> p.getPurchaser().getId() == userId))
                    .collect(Collectors.toList());

    }

    @Override
    public void addParcel(int shipmentId, Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            Shipment shipment = session.get(Shipment.class, shipmentId);
            if (shipment == null) {
                throw new EntityNotFoundException("Shipment", shipmentId);
            }
            boolean exists = shipment.getParcels().stream()
                    .anyMatch(p -> p.getId() == parcel.getId());
            if (exists) {
                throw new EntityUpdateException(ADD_PARCEL_ERROR);
            }
            shipment.addParcel(parcel);
            session.beginTransaction();
            session.update(shipment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeParcel(int shipmentId, Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            Shipment shipment = session.get(Shipment.class, shipmentId);
            if (shipment == null) {
                throw new EntityNotFoundException("Shipment", shipmentId);
            }

            boolean exists = shipment.getParcels().stream()
                    .anyMatch(p -> p.getId() == parcel.getId());

            if (!exists) {
                throw new EntityUpdateException(REMOVE_PARCEL_ERROR);
            }
            shipment.removeParcel(parcel);
            session.beginTransaction();
            session.update(shipment);
            session.getTransaction().commit();
        }
    }

    @Override
    public Status checkShipmentStatus(int shipmentId) {
        try (Session session = sessionFactory.openSession()) {
            Shipment shipment = session.get(Shipment.class, shipmentId);
            if (shipment == null) {
                throw new EntityNotFoundException("Shipment", shipmentId);
            }
            return shipment.getStatus();
        }
    }

    @Override
    public List<Shipment> filterShipmentsByWarehouse(Integer originWarehouseId, Integer destinationWarehouseId, Integer customerId) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> filter = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            if (customerId != null) {
                filter.add(" customerId = :customerId ");
                params.put("customerId", customerId);
            }

            if (originWarehouseId != null) {
                filter.add(" origin_warehouse_id = :originWarehouseId ");
                params.put("originWarehouseId", originWarehouseId);
            }

            if (destinationWarehouseId != null) {
                filter.add(" destination_warehouse_id = :destinationWarehouseId ");
                params.put("destinationWarehouseId", destinationWarehouseId);
            }

            if (customerId == null && originWarehouseId == null && destinationWarehouseId == null) {
                stringBuilder.append(" from Shipment ");
            } else {
                stringBuilder.append(" from Shipment where ").append(String.join(" and ", filter));
            }

            Query<Shipment> query = session.createQuery(stringBuilder.toString(), Shipment.class);

            query.setProperties(params);

            return query.list();
        }
    }
}
