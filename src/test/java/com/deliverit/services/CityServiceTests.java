package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.repositories.city.CityRepository;
import com.deliverit.services.city.CityServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.deliverit.Helpers.createMockCity;

@ExtendWith(MockitoExtension.class)
public class CityServiceTests {

    @Mock
    CityRepository mockRepository;

    @InjectMocks
    CityServiceImpl service;

    @Test
    public void getAll_Should_Return_AllCities() {
        var city = createMockCity();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(city));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_City_WhenMatchExists() {
        var city = createMockCity();

        Mockito.when(mockRepository.getById(city.getId()))
                .thenReturn(city);

        Assertions.assertDoesNotThrow(() -> service.getById(city.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(city.getId());
    }

    @Test
    public void create_Should_Throw_When_CityNameIsTaken() {
        var city = createMockCity();

        Mockito.when(mockRepository.getByName(city.getName()))
                .thenReturn(city);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(city));
    }

    @Test
    public void create_ShouldCallRepository_When_CityDoesNotExists() {
        var city = createMockCity();

        Mockito.when(mockRepository.getByName(city.getName()))
                .thenThrow(new EntityNotFoundException("City", "name", city.getName()));

        Assertions.assertDoesNotThrow(() -> service.create(city));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(City.class));
    }


    @Test
    public void update_Should_Throw_When_CityNameIsTaken() {
        var city = createMockCity();

        Mockito.when(mockRepository.getByName(city.getName()))
                .thenReturn(city);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(city));
    }

    @Test
    public void update_Should_Throw_When_CityNameDoesNotExists() {
        var city = createMockCity();

        Mockito.when(mockRepository.getByName(city.getName()))
                .thenThrow(new EntityNotFoundException("City", "name", city.getName()));

        Assertions.assertDoesNotThrow(() -> service.update(city));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(City.class));
    }

    @Test
    public void delete_Should_Delete_City() {
        var city = createMockCity();

        Assertions.assertDoesNotThrow(() -> service.delete(city.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(city.getId());
    }
}
