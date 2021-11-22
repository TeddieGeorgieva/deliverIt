package com.deliverit.repositories.warehouse;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.models.Shipment;
import com.deliverit.models.Warehouse;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public WarehouseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Warehouse> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery("from Warehouse", Warehouse.class);
            return query.list();
        }
    }

    @Override
    public Warehouse getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Warehouse warehouse = session.get(Warehouse.class, id);
            if (warehouse == null) {
                throw new EntityNotFoundException("Warehouse", id);
            }
            return warehouse;
        }
    }

    @Override
    public Warehouse getByAddress(Address address) {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery("from Warehouse where address.city = :city " +
                    "and address.streetName = :streetName", Warehouse.class);
            query.setParameter("city", address.getCity());
            query.setParameter("streetName", address.getStreetName());

            List<Warehouse> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Address", "street", address.getStreetName());
            }

            return result.get(0);
        }
    }

    // todo: get next arriving shipmentS, not just the first one
    @Override
    public Shipment getNextArriving(int id) {
        try (Session session = sessionFactory.openSession()) {
            if (session.get(Warehouse.class, id) == null) {
                throw new EntityNotFoundException("Warehouse", id);
            }

            Query<Shipment> query = session.createQuery("from Shipment " +
                    "where destinationWarehouse.id = :id " +
                    "and arrivalDate >= current_date order by arrivalDate asc", Shipment.class).setMaxResults(1);

            query.setParameter("id", id);
            List<Shipment> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("There are no shipments arriving in the specified warehouse.");
            }

            return result.get(0);
        }
    }

    @Override
    public void create(Warehouse warehouse) {
        try (Session session = sessionFactory.openSession()) {
            session.save(warehouse);
        }
    }

    @Override
    public void update(Warehouse warehouse) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(warehouse);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Warehouse toDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();
        }
    }
}
