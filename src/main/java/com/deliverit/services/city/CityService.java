package com.deliverit.services.city;

import com.deliverit.models.City;

import java.util.List;

public interface CityService {

    List<City> getAll();

    City getById(int id);

    void create(City city);

    void update(City city);

    void delete(int id);

}
