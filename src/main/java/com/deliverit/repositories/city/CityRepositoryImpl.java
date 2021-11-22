package com.deliverit.repositories.city;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CityRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<City> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("from City", City.class);
            return query.list();
        }
    }

    @Override
    public City getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            City city = session.get(City.class, id);
            if (city == null) {
                throw new EntityNotFoundException("City", id);
            }
            return city;
        }
    }

    @Override
    public City getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("from City where name = :name", City.class);
            query.setParameter("name", name);

            List<City> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("City", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(City city) {
        try (Session session = sessionFactory.openSession()) {
            session.save(city);
        }
    }

    @Override
    public void update(City city) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(city);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        City cityToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(cityToDelete);
            session.getTransaction().commit();
        }
    }
}
