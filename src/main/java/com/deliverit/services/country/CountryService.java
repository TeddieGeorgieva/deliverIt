package com.deliverit.services.country;

import com.deliverit.models.Country;

import java.util.List;

public interface CountryService {

    List<Country> getAll();

    Country getById(int id);

    void create(Country country);

    void update(Country country);

    void delete(int id);
}
