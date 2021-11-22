package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Warehouse;
import com.deliverit.repositories.warehouse.WarehouseRepository;
import com.deliverit.services.warehouse.WarehouseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTests {

    @Mock
    WarehouseRepository mockRepository;

    @InjectMocks
    WarehouseServiceImpl service;

    @Test
    public void create_Should_Throw_When_WarehouseAddressIsTaken() {
        var warehouse = createMockWarehouse();
        var user = createMockEmployeeUser();

        Mockito.when(mockRepository.getByAddress(warehouse.getAddress()))
                .thenReturn(warehouse);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user, warehouse));
    }
    @Test
    public void create_ShouldCallRepository_When_WarehouseDoesNotExists() {
        var warehouse = createMockWarehouse();
        var user = createMockEmployeeUser();

        Mockito.when(mockRepository.getByAddress(warehouse.getAddress()))
                .thenThrow(new EntityNotFoundException("Warehouse", "address", warehouse.getAddress().toString()));

        Assertions.assertDoesNotThrow(() -> service.create(user, warehouse));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Warehouse.class));
    }
    @Test
    public void update_Should_Throw_When_WarehouseAddressIsTaken() {
        var warehouse = createMockWarehouse();
        var user = createMockEmployeeUser();

        Mockito.when(mockRepository.getByAddress(warehouse.getAddress()))
                .thenReturn(warehouse);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(warehouse, user));
    }
    @Test
    public void update_Should_Throw_When_WarehouseAddressDoesNotExists() {
        var warehouse = createMockWarehouse();
        var user = createMockEmployeeUser();

        Mockito.when(mockRepository.getByAddress(warehouse.getAddress()))
                .thenThrow(new EntityNotFoundException("Warehouse", "address", warehouse.getAddress().toString()));

        Assertions.assertDoesNotThrow(() -> service.update(warehouse, user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Warehouse.class));
    }
    @Test
    public void throwIfNotEmployee_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        var warehouse = createMockWarehouse();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(user, warehouse));
    }
    @Test
    public void getNextArrivingShipment_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getNextArrivingShipment(createMockShipment().getId(), user));
    }
//    @Test
//    public void getAll_Should_Throw_WhenUserNotEmployee() {
//        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getAll());
//    }
    @Test
    public void delete_Should_Delete_Warehouse() {
        var user = createMockEmployeeUser();
        var warehouse = createMockWarehouse();

        Assertions.assertDoesNotThrow(() -> service.delete(warehouse.getId(), user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(warehouse.getId());
    }
    @Test
    public void getAll_Should_Return_AllWarehouses() {
        var warehouse = createMockWarehouse();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(warehouse));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Warehouse_WhenMatchExists() {
        var warehouse = createMockWarehouse();

        Mockito.when(mockRepository.getById(warehouse.getId()))
                .thenReturn(warehouse);

        Assertions.assertDoesNotThrow(() -> service.getById(warehouse.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(warehouse.getId());
    }
    @Test
    public void getNextArrivingShipment_Should_Return_NextArrivingShipment() {
        var shipment = createMockShipment();

        Mockito.when(mockRepository.getNextArriving(shipment.getId()))
                .thenReturn(shipment);

        Assertions.assertDoesNotThrow(() -> service.getNextArrivingShipment(shipment.getId(), createMockEmployeeUser()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getNextArriving(shipment.getId());
    }
}
