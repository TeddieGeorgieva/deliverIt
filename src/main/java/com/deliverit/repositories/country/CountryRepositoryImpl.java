package com.deliverit.repositories.country;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country", Country.class);
            return query.list();
        }
    }

    @Override
    public Country getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Country country = session.get(Country.class, id);
            if (country == null) {
                throw new EntityNotFoundException("Country", id);
            }
            return country;
        }
    }

    @Override
    public Country getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country where name = :name", Country.class);
            query.setParameter("name", name);

            List<Country> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Country", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.save(country);
        }
    }

    @Override
    public void update(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(country);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Country countryToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(countryToDelete);
            session.getTransaction().commit();
        }
    }
}
