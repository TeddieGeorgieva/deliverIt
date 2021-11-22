package com.deliverit.services.country;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Country;
import com.deliverit.repositories.country.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.getAll();
    }

    @Override
    public Country getById(int id) {
        return countryRepository.getById(id);
    }

    @Override
    public void create(Country country) {
        boolean duplicateExists = true;
        try {
            countryRepository.getByName(country.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        countryRepository.create(country);
    }

    @Override
    public void update(Country country) {
        boolean duplicateExists = true;
        try {
            countryRepository.getByName(country.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        countryRepository.update(country);
    }

    @Override
    public void delete(int id) {
        countryRepository.delete(id);
    }
}
