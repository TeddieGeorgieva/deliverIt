package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Category;
import com.deliverit.repositories.category.CategoryRepository;
import com.deliverit.services.category.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.deliverit.Helpers.createMockAddress;
import static com.deliverit.Helpers.createMockCategory;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    CategoryRepository mockRepository;

    @InjectMocks
    CategoryServiceImpl service;

    @Test
    public void getAll_Should_Return_AllCategories() {
        var category = createMockCategory();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(category));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Category_WhenMatchExists() {
        var category = createMockCategory();

        Mockito.when(mockRepository.getById(category.getId()))
                .thenReturn(category);

        Assertions.assertDoesNotThrow(() -> service.getById(category.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(category.getId());
    }
    @Test
    public void create_Should_Throw_When_CategoryNameIsTaken() {
        var category = createMockCategory();
        Mockito.when(mockRepository.getByName(category.getName()))
                .thenReturn(category);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(category));
    }

    @Test
    public void create_ShouldCallRepository_When_CategoryDoesNotExists() {
        var category = createMockCategory();

        Mockito.when(mockRepository.getByName(category.getName()))
                .thenThrow(new EntityNotFoundException("Category", "name", category.getName()));

        Assertions.assertDoesNotThrow(() -> service.create(category));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Category.class));
    }


    @Test
    public void update_Should_Throw_When_CategoryNameIsTaken() {
        var category = createMockCategory();

        Mockito.when(mockRepository.getByName(category.getName()))
                .thenReturn(category);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(category));
    }
    @Test
    public void update_Should_Throw_When_CategoryNameDoesNotExists() {
        var category = createMockCategory();

        Mockito.when(mockRepository.getByName(category.getName()))
                .thenThrow(new EntityNotFoundException("Category", "name", category.getName()));

        Assertions.assertDoesNotThrow(() -> service.update(category));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Category.class));
    }
    @Test
    public void delete_Should_Delete_Category() {
        var category = createMockCategory();

        Assertions.assertDoesNotThrow(() -> service.delete(category.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(category.getId());
    }
}
