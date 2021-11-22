package com.deliverit.services;

import com.deliverit.repositories.address.AddressRepository;
import com.deliverit.services.address.AddressServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.deliverit.Helpers.createMockAddress;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTests {

    @Mock
    AddressRepository mockRepository;

    @InjectMocks
    AddressServiceImpl service;

    @Test
    public void getAll_Should_Return_AllAddresses() {
        var address = createMockAddress();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(address));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Address_WhenMatchExists() {
        var address = createMockAddress();

        Mockito.when(mockRepository.getById(address.getId()))
                .thenReturn(address);

        Assertions.assertDoesNotThrow(() -> service.getById(address.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(address.getId());
    }
    @Test
    public void create_Should_Create_Address() {
        var address = createMockAddress();

        Assertions.assertDoesNotThrow(() -> service.create(address));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(address);
    }
    @Test
    public void update_Should_Update_Address() {
        var address = createMockAddress();

        Assertions.assertDoesNotThrow(() -> service.update(address));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(address);
    }
    @Test
    public void delete_Should_Delete_Address() {
        var address = createMockAddress();

        Assertions.assertDoesNotThrow(() -> service.delete(address.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(address.getId());
    }
}
