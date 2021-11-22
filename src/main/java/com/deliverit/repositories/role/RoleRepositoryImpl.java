package com.deliverit.repositories.role;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Role> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role", Role.class);
            return query.list();
        }
    }

    @Override
    public Role getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Role role = session.get(Role.class, id);
            if (role == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return role;
        }

    }

    @Override
    public Role getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role where name = :name", Role.class);
            query.setParameter("name", name);

            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.save(role);
        }
    }

    @Override
    public void update(Role role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(role);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Role roleToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(roleToDelete);
            session.getTransaction().commit();
        }
    }


}
