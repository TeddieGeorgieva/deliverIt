package com.deliverit.repositories.users;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> users = query.list();
            if (users.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return users.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        User userToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(userToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> search(Optional<String> search) {
        try (Session session = sessionFactory.openSession()) {
            //by lastname, firstname, email
            Query<User> query = session.createQuery(
                    "from User where firstName like :name or lastName like :name or email like :name", User.class);
            query.setParameter("name", "%" + search.get() + "%");

            return query.list();
        }
    }

    @Override
    public int getNumberOfUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> q = session.createQuery("select username from User ");
            return q.list().size();
        }
    }

    @Override
    public List<User> filter(String firstName, String lastName, String email, String searchAll) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> filter = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            if (searchAll != null && (firstName != null || lastName != null || email != null)) {
                //todo return error
            }

            if (searchAll != null && !searchAll.equals("")) {

                    filter.add(" email like :email ");
                    filter.add(" firstName like :firstName ");
                    filter.add(" lastName like :lastName ");

                    params.put("firstName", "%" + searchAll + "%");
                    params.put("lastName", "%" + searchAll + "%");
                    params.put("email", "%" + searchAll + "%");

                    stringBuilder.append(" from User where ").append(String.join(" or ", filter));
            } else {
                if (firstName != null) {
                    if (!firstName.equals("")) {
                        filter.add(" firstName like :firstName ");
                        params.put("firstName", "%" + firstName + "%");
                    }
                }

                if (lastName != null) {
                    if (!lastName.equals("")) {
                        filter.add(" lastName like :lastName ");
                        params.put("lastName", "%" + lastName + "%");
                    }
                }

                if (email != null) {
                    if (!email.equals("")) {
                        filter.add(" email like :email ");
                        params.put("email", "%" + email + "%");
                    }
                }
                if (firstName.equals("") && lastName.equals("") && email.equals("")) {
                    stringBuilder.append(" from User ");
                } else {
                    stringBuilder.append(" from User where ").append(String.join(" and ", filter));
                }
            }


            Query<User> query = session.createQuery(stringBuilder.toString(), User.class);

            query.setProperties(params);

            return query.list();
        }
    }
}
