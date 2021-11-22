package com.deliverit.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CountryDto {

    @NotNull(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Country name must be between 2 and 20 symbols.")
    private String name;

    public CountryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
