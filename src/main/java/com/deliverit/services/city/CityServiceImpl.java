package com.deliverit.services.city;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.repositories.city.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override
    public List<City> getAll() {
        return cityRepository.getAll();
    }

    @Override
    public City getById(int id) {
        return cityRepository.getById(id);
    }

    @Override
    public void create(City city) {
        boolean duplicateExists = true;
        try {
            cityRepository.getByName(city.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }

        cityRepository.create(city);

    }

    @Override
    public void update(City city) {
        boolean duplicateExists = true;
        try {
            cityRepository.getByName(city.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }
        cityRepository.update(city);
    }
    @Override
    public void delete(int id) {
        cityRepository.delete(id);
    }
}
