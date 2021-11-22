package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.models.Country;
import com.deliverit.repositories.country.CountryRepository;
import com.deliverit.services.country.CountryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTests {

    @Mock
    CountryRepository mockRepository;

    @InjectMocks
    CountryServiceImpl service;

    @Test
    public void getAll_Should_Return_AllCountries() {
        var country = createMockCountry();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(country));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Country_WhenMatchExists() {
        var country = createMockCountry();

        Mockito.when(mockRepository.getById(country.getId()))
                .thenReturn(country);

        Assertions.assertDoesNotThrow(() -> service.getById(country.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(country.getId());
    }
    @Test
    public void create_Should_Throw_When_CountryNameIsTaken() {
        var country = createMockCountry();
        Mockito.when(mockRepository.getByName(country.getName()))
                .thenReturn(country);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(country));
    }

    @Test
    public void create_ShouldCallRepository_When_CountryDoesNotExists() {
        var mockCountry = createMockCountry();

        Mockito.when(mockRepository.getByName(mockCountry.getName()))
                .thenThrow(new EntityNotFoundException("Country", "name", mockCountry.getName()));

        Assertions.assertDoesNotThrow(() -> service.create(mockCountry));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Country.class));
    }


    @Test
    public void update_Should_Throw_When_CountryNameIsTaken() {
        Country country = createMockCountry();
        Mockito.when(mockRepository.getByName(country.getName()))
                .thenReturn(country);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(country));
    }
    @Test
    public void update_Should_Throw_When_CountryNameDoesNotExists() {
        var mockCountry = createMockCountry();

        Mockito.when(mockRepository.getByName(mockCountry.getName()))
                .thenThrow(new EntityNotFoundException("Country", "name", mockCountry.getName()));

        Assertions.assertDoesNotThrow(() -> service.update(mockCountry));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Country.class));
    }
    @Test
    public void delete_Should_Delete_Country() {
        var mockCountry = createMockCountry();

        Assertions.assertDoesNotThrow(() -> service.delete(mockCountry.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockCountry.getId());
    }
}
