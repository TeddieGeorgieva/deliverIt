package com.deliverit.services;

import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.repositories.parcel.ParcelRepository;
import com.deliverit.repositories.shipment.ShipmentRepository;
import com.deliverit.services.parcel.ParcelServiceImpl;
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
public class ParcelServiceTests {

    @Mock
    ParcelRepository mockRepository;

    @Mock
    ShipmentRepository shipmentRepository;

    @InjectMocks
    ParcelServiceImpl service;

    @Test
    public void getAll_Should_Throw_WhenUserNotEmployee() {
        var user = createMockUser();
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.getAll(user));
    }

    @Test
    public void getAll_Should_Return_AllParcels() {
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(parcel));

        Assertions.assertDoesNotThrow(() -> service.getAll(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Parcel_WhenMatchExists() {
        var parcel = createMockParcel();


        Mockito.when(mockRepository.getById(parcel.getId()))
                .thenReturn(parcel);

        Assertions.assertDoesNotThrow(() -> service.getById(parcel.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(parcel.getId());
    }
    @Test
    public void create_ShouldCallRepository_When_ParcelDoesNotExists() {
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.create(parcel, user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Parcel.class));
    }
    @Test
    public void delete_Should_Delete_Parcel() {
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.delete(parcel.getId(), user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(parcel.getId());
    }

    @Test
    public void update_Should_Update_When_UserIsEmployee() {
        var parcel = createMockParcel();
        var user = createMockEmployeeUser();

        Assertions.assertDoesNotThrow(() -> service.update(parcel, user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(parcel);
    }

    @Test
    public void update_Should_Throw_When_UserIsNotAuthorized() {
        var parcel = createMockParcel();
        var user = createMockUser();

        parcel.getPurchaser().setUsername("mock");

        Mockito.when(mockRepository.getById(parcel.getId()))
                .thenReturn(parcel);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.update(parcel, user));
    }
    @Test
    public void filter_Should_FilterParcels_When_UserIsEmployee() {
        var user = createMockEmployeeUser();
        var parcel = createMockParcel();

        Mockito.when(mockRepository.filter(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()))
                .thenReturn(List.of(parcel));

        Assertions.assertDoesNotThrow(() -> service.filter(user,Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());
    }
    @Test
    public void filter_Should_Throw_When_UserIsNotEmployee() {

        var user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.filter(user,Optional.empty(), Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty()));
    }
    @Test
    public void search_Should_Return_AllParcels_When_SearchIsEmpty() {

       Assertions.assertDoesNotThrow(() -> service.search(Optional.empty()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void search_Should_Return_SearchedParcels_When_SearchIsNotEmpty() {
        var parcel = createMockParcel();

        Mockito.when(mockRepository.search("bla"))
                .thenReturn(List.of(parcel));
        Assertions.assertDoesNotThrow(() -> service.search(Optional.of("bla")));

        Mockito.verify(mockRepository, Mockito.times(1))
                .search("bla");
    }
//    @Test
//    public void checkShipmentStatus_Should_Return_ShipmentStatus() {
//        var parcel = createMockParcel();
//        var user = createMockUser();
//        var shipment = createMockShipment();
//        parcel.setShipments(List.of(shipment));
//        parcel.getShipments().get(0).addParcel(parcel);
//
//        Mockito.when(mockRepository.getById(parcel.getId()))
//                .thenReturn(parcel);
//
//        Assertions.assertDoesNotThrow(() -> service.checkShipmentStatus(user, parcel.getId()));
//
//        Mockito.verify(shipmentRepository, Mockito.times(1))
//                .checkShipmentStatus(shipment.getId());
//    }

    @Test
    public void checkShipmentStatus_Should_Throw_When_UserNotAuthorized() {
        var parcel = createMockParcel();
        var user = createMockUser();
        parcel.getPurchaser().setId(3);

        Mockito.when(mockRepository.getById(parcel.getId()))
                .thenReturn(parcel);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.checkShipmentStatus(user, parcel.getId()));
    }
}
