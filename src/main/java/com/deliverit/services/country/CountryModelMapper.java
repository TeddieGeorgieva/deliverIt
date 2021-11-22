package com.deliverit.services.country;

import com.deliverit.models.Country;
import com.deliverit.models.CountryDto;
import com.deliverit.repositories.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryModelMapper {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryModelMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    public Country fromDto(CountryDto countryDto) {
        Country country = new Country();
        dtoToObject(countryDto, country);
        return country;
    }

    public Country fromDto(CountryDto countryDto, int id) {
        Country country = countryRepository.getById(id);
        dtoToObject(countryDto, country);
        return country;
    }

    private void dtoToObject(CountryDto countryDto, Country country) {
        country.setName(countryDto.getName());
    }

}
