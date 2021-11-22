package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Country;
import com.deliverit.models.Shipment;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import com.deliverit.services.shipment.ShipmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTests {

    @Mock
    ShipmentRepository shipmentRepository;

    @Mock
    ParcelRepository parcelRepository;

    @InjectMocks
    ShipmentServiceImpl service;

    @Test
    public void getAll_Should_Return_AllShipments() {
        var shipment = createMockShipment();
        var user = createMockEmployeeUser();

        Mockito.when(shipmentRepository.getAll())
                .thenReturn(List.of(shipment));

        Assertions.assertDoesNotThrow(() -> service.getAll(user));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Country_WhenMatchExists() {
        var shipment = createMockShipment();

        Mockito.when(shipmentRepository.getById(shipment.getId()))
                .thenReturn(shipment);

        Assertions.assertDoesNotThrow(() -> service.getById(shipment.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getById(shipment.getId());
    }
    @Test
    public void create_ShouldCallRepository_When_CountryDoesNotExists() {
        var shipment = createMockShipment();
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.create(user, shipment));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .create(Mockito.any(Shipment.class));
    }
    @Test
    public void update_Should_Update_When_UserIsEmployee() {
        var shipment = createMockShipment();
        var user = createMockEmployeeUser();
        Assertions.assertDoesNotThrow(() -> service.update(user, shipment));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .update(Mockito.any(Shipment.class));
    }
    @Test
    public void delete_Should_Delete_Shipment() {
        var shipment = createMockShipment();

        Assertions.assertDoesNotThrow(() -> service.delete(shipment.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .delete(shipment.getId());
    }
    @Test
    public void getShipmentsOnTheWay_Should_Return_ShipmentsOnTheWay() {

        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.getShipmentsOnTheWay(user));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getShipmentsOnTheWay(user);
    }
    @Test
    public void filterByCustomer_Should_Return_ShipmentsFilterByCustomer() {
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.filterByCustomer(user, user.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .filterByCustomer(user.getId());
    }
    @Test
    public void filter_Should_Return_FilteredShipment() {
        var shipment = createMockShipment();
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.filter(user, Optional.empty(), Optional.empty()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty());
    }
    @Test
    public void addParcel_Should_Add_ParcelToShipment() {
        var shipment = createMockShipment();
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Mockito.when(parcelRepository.getById(parcel.getId()))
                .thenReturn(parcel);

        Assertions.assertDoesNotThrow(() -> service.addParcel(user, shipment.getId(), parcel.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .addParcel(shipment.getId(), parcel);
    }
    @Test
    public void removeParcel_Should_Remove_ParcelFromShipment() {
        var shipment = createMockShipment();
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Mockito.when(parcelRepository.getById(parcel.getId()))
                .thenReturn(parcel);

        Assertions.assertDoesNotThrow(() -> service.removeParcel(user, shipment.getId(), parcel.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .removeParcel(shipment.getId(), parcel);
    }
    @Test
    public void getAll_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getAll(user));
    }
    @Test
    public void getShipmentsOnTheWay_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getShipmentsOnTheWay(user));
    }
    @Test
    public void create_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.create(user, createMockShipment()));
    }
    @Test
    public void update_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(user, createMockShipment()));
    }
    @Test
    public void filter_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.filter(user, Optional.empty(), Optional.empty()));
    }
    @Test
    public void filterByCustomer_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.filterByCustomer(user, user.getId()));
    }
    @Test
    public void addParcel_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.addParcel(user, createMockShipment().getId(), createMockParcel().getId()));
    }
    @Test
    public void removeParcel_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.removeParcel(user, createMockShipment().getId(), createMockParcel().getId()));
    }

    @Test
    public void checkShipmentStatus_Should_Return_ShipmentStatus() {
        var shipment = createMockShipment();

        Mockito.when(shipmentRepository.checkShipmentStatus(shipment.getId()))
                .thenReturn(shipment.getStatus());

        Assertions.assertDoesNotThrow(() -> service.checkShipmentStatus(shipment.getId()));

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .checkShipmentStatus(shipment.getId());
    }
}
