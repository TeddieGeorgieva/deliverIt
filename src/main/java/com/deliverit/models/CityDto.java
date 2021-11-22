package com.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CityDto {

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "City name must be between 2 and 20 symbols.")
    private String name;

    @NotNull(message = "Country cannot be blank.")
    private int countryId;

    public CityDto() {
    }

    public CityDto(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
