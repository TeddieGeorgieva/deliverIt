package com.deliverit.services.address;

import com.deliverit.models.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAll();

    Address getById(int id);

    void create(Address address);

    void update(Address address);

    void delete(int id);
}
