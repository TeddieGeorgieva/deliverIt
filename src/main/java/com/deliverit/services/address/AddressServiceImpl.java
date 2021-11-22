package com.deliverit.services.address;

import com.deliverit.models.Address;
import com.deliverit.repositories.address.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAll() {
        return addressRepository.getAll();
    }

    @Override
    public Address getById(int id) {
        return addressRepository.getById(id);
    }

    @Override
    public void create(Address address) {
        addressRepository.create(address);
    }

    @Override
    public void update(Address address) {
        addressRepository.update(address);
    }

    @Override
    public void delete(int id) {
        addressRepository.delete(id);
    }
}
