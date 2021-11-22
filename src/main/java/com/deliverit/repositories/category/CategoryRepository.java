package com.deliverit.repositories.category;

import com.deliverit.models.Category;


import java.util.List;

public interface CategoryRepository {

    List<Category> getAll();

    Category getById(int id);

    Category getByName(String name);

    void create(Category category);

    void update(Category category);

    void delete(int id);
}
