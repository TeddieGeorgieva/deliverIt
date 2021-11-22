package com.deliverit.controllers.rest;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.models.AddressDto;
import com.deliverit.services.address.AddressModelMapper;
import com.deliverit.services.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressModelMapper addressModelMapper;

    public AddressController(AddressService addressService, AddressModelMapper addressModelMapper) {
        this.addressService = addressService;
        this.addressModelMapper = addressModelMapper;
    }

    @GetMapping
    public List<Address> getAll() {
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public Address getById(@PathVariable int id) {
        try {
            return addressService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Address create(@Valid @RequestBody AddressDto addressDto) {
        try {
            Address address = addressModelMapper.fromDto(addressDto);
            addressService.create(address);
            return address;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Address update(@PathVariable int id, @Valid @RequestBody AddressDto addressDto) {
        try {
            Address address = addressModelMapper.fromDto(addressDto, id);
            addressService.update(address);
            return address;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            addressService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
