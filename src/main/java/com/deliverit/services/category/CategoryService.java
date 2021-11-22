package com.deliverit.services.category;

import com.deliverit.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(int id);

    void create(Category category);

    void update(Category category);

    void delete(int id);
}
