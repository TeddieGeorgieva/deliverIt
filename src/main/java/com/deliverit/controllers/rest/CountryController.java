package com.deliverit.controllers.rest;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Country;
import com.deliverit.models.CountryDto;
import com.deliverit.services.country.CountryModelMapper;
import com.deliverit.services.country.CountryService;
import com.deliverit.services.category.CategoryModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService service;
    private final CountryModelMapper modelMapper;

    @Autowired
    public CountryController(CountryService service, CountryModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Country createCountry(@Valid @RequestBody CountryDto countryDto) {
        try {
            Country country = modelMapper.fromDto(countryDto);
            service.create(country);
            return country;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Country update(@PathVariable int id, @Valid @RequestBody CountryDto countryDto) {
        try {
            Country country = modelMapper.fromDto(countryDto, id);
            service.update(country);
            return country;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
