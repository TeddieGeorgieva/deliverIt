package com.deliverit.services.address;

import com.deliverit.models.Address;
import com.deliverit.models.AddressDto;
import com.deliverit.models.City;
import com.deliverit.repositories.address.AddressRepository;
import com.deliverit.repositories.city.CityRepository;
import org.springframework.stereotype.Component;

@Component
public class AddressModelMapper {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    public AddressModelMapper(AddressRepository addressRepository, CityRepository cityRepository) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
    }
    public Address fromDto(AddressDto addressDto) {
        Address address = new Address();
        dtoToObject(addressDto, address);
        return address;
    }

    public Address fromDto(AddressDto addressDto, int id) {
        Address address = addressRepository.getById(id);
        dtoToObject(addressDto, address);
        return address;
    }

    private void dtoToObject(AddressDto addressDto, Address address) {
        City city = cityRepository.getById(address.getId());
        address.setStreetName(addressDto.getStreetName());
        address.setCity(city);
    }
}
