package com.deliverit.repositories.category;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Category;
import com.deliverit.models.CategoryDto;
import com.deliverit.repositories.category.CategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CategoryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Category> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery("from Category", Category.class);
            return query.list();
        }
    }

    @Override
    public Category getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Category category = session.get(Category.class, id);
            if (category == null) {
                throw new EntityNotFoundException("Category", id);
            }
            return category;
        }
    }

    @Override
    public Category getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery("from Category where name = :name", Category.class);
            query.setParameter("name", name);

            List<Category> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Category", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.save(category);
        }
    }

    @Override
    public void update(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(category);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Category categoryToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(categoryToDelete);
            session.getTransaction().commit();
        }
    }
}
