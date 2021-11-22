package com.deliverit.controllers.rest;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.models.CityDto;
import com.deliverit.services.city.CityModelMapper;
import com.deliverit.services.city.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService service;
    private final CityModelMapper modelMapper;

    @Autowired
    public CityController(CityService service, CityModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<City> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public City getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public City create(@Valid @RequestBody CityDto cityDto) {
        try {
            City city = modelMapper.fromDto(cityDto);
            service.create(city);
            return city;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public City update(@PathVariable int id, @Valid @RequestBody CityDto cityDto) {
        try {
            City city = modelMapper.fromDto(cityDto, id);
            service.update(city);
            return city;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
