package com.deliverit;

import com.deliverit.models.*;

import java.time.LocalDate;
import java.util.*;

public class Helpers {
    public static final Set<Parcel> parcels = new HashSet<>();
    public static final List<Shipment> shipments = new ArrayList<>();
    public static Country createMockCountry() {
        var mockCountry = new Country();
        mockCountry.setId(1);
        mockCountry.setName("mockCountry");
        return mockCountry;
    }
    public static City createMockCity() {
        var mockCity = new City();
        mockCity.setId(1);
        mockCity.setName("mockCity");
        mockCity.setCountry(createMockCountry());
        return mockCity;
    }
    public static Address createMockAddress() {
        var mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setStreetName("MockStreet");
        mockAddress.setCity(createMockCity());
        return mockAddress;
    }

    public static Category createMockCategory() {
        var mockCategory = new Category();
        mockCategory.setId(1);
        mockCategory.setName("MockCategory");
        return mockCategory;
    }

    public static Parcel createMockParcel() {
        var mockParcel = new Parcel();
        mockParcel.setId(1);
        mockParcel.setPurchaser(createMockUser());
        mockParcel.setDestinationWarehouse(createMockWarehouse());
        mockParcel.setWeight(1.5);
        mockParcel.setCategory(createMockCategory());
        mockParcel.setDeliveryType(ParcelDeliveryType.PICK_UP_FROM_WAREHOUSE);
        mockParcel.setShipments(shipments);
        return mockParcel;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mockUserEmail");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastname");
        mockUser.setRole(createMockRole());
        mockUser.setAddressForDelivery(createMockAddress());
        return mockUser;
    }

    public static User createMockEmployeeUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mockUserEmail");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastname");
        mockUser.setRole(createMockEmployeeRole());
        mockUser.setAddressForDelivery(createMockAddress());
        return mockUser;
    }


    public static Warehouse createMockWarehouse() {
        var mockWarehouse = new Warehouse();
        mockWarehouse.setId(1);
        mockWarehouse.setAddress(createMockAddress());
        return mockWarehouse;
    }

    public static Shipment createMockShipment() {
        var mockShipment = new Shipment();
        mockShipment.setId(1);
        mockShipment.setDestinationWarehouse(createMockWarehouse());
        mockShipment.setOriginWarehouse(createMockWarehouse());
        mockShipment.setDepartureDate(LocalDate.now());
        mockShipment.setArrivalDate(LocalDate.now());
        mockShipment.setStatus(Status.PREPARING);
        mockShipment.setParcels(parcels);
        return mockShipment;
    }

    public static Role createMockRole() {
        var mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("customer");
        return mockRole;
    }

    public static Role createMockEmployeeRole() {
        var mockRole = new Role();
        mockRole.setId(2);
        mockRole.setName("employee");
        return mockRole;
    }

}
