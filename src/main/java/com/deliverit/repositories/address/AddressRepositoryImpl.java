package com.deliverit.repositories.address;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public AddressRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Address> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("from Address", Address.class);
            return query.list();
        }
    }

    @Override
    public Address getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Address address = session.get(Address.class, id);
            if (address == null) {
                throw new EntityNotFoundException("Address", id);
            }
            return address;
        }
    }


    @Override
    public void create(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.save(address);
        }
    }

    @Override
    public void update(Address address) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(address);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Address addressToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(addressToDelete);
            session.getTransaction().commit();
        }
    }
}
