package com.deliverit.repositories.address;

import com.deliverit.models.Address;

import java.util.List;

public interface AddressRepository {
    List<Address> getAll();

    Address getById(int id);

    void create(Address address);

    void update(Address address);

    void delete(int id);
}
