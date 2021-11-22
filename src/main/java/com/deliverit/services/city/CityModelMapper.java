package com.deliverit.services.city;

import com.deliverit.models.City;
import com.deliverit.models.CityDto;
import com.deliverit.models.Country;
import com.deliverit.repositories.city.CityRepository;
import com.deliverit.repositories.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityModelMapper {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public CityModelMapper(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public City fromDto(CityDto cityDto) {
        City city = new City();
        dtoToObject(cityDto, city);
        return city;
    }

    public City fromDto(CityDto cityDto, int id) {
        City city = cityRepository.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(CityDto cityDto, City city) {
        Country country = countryRepository.getById(cityDto.getCountryId());
        city.setName(cityDto.getName());
        city.setCountry(country);
    }
}
