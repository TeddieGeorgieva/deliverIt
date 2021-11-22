package com.deliverit.repositories.country;

import com.deliverit.models.Country;

import java.util.List;

public interface CountryRepository {

    List<Country> getAll();

    Country getById(int id);

    Country getByName(String name);

    void create(Country country);

    void update(Country country);

    void delete(int id);
}
