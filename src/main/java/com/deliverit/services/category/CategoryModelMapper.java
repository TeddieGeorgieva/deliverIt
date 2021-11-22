package com.deliverit.services.category;

import com.deliverit.models.*;
import com.deliverit.repositories.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelMapper {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryModelMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category fromDto(CategoryDto categoryDto) {
        Category category = new Category();
        dtoToObject(categoryDto, category);
        return category;
    }

    public Category fromDto(CategoryDto categoryDto, int id) {
        Category category = categoryRepository.getById(id);
        dtoToObject(categoryDto, category);
        return category;
    }

    private void dtoToObject(CategoryDto categoryDto, Category category) {
        category.setName(categoryDto.getName());
    }
}