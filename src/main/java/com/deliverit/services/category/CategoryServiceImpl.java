package com.deliverit.services.category;


import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Category;
import com.deliverit.repositories.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() { return categoryRepository.getAll(); }

    @Override
    public Category getById(int id) {
        return categoryRepository.getById(id);
    }

    @Override
    public void create(Category category) {
        boolean duplicateExists = true;
        try {
            categoryRepository.getByName(category.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Category", "name", category.getName());
        }

        categoryRepository.create(category);
    }

    @Override
    public void update(Category category) {
        boolean duplicateExists = true;
        try {
            categoryRepository.getByName(category.getName());
//            if (existingCategory.getId() == category.getId()) {
//                duplicateExists = false;
//            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Category", "name", category.getName());
        }

        categoryRepository.update(category);
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
