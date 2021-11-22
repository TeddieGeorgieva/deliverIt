package com.deliverit.repositories.city;

import com.deliverit.models.City;

import java.util.List;

public interface CityRepository {

    List<City> getAll();

    City getById(int id);

    City getByName(String name);

    void create(City city);

    void update(City city);

    void delete(int id);

}
