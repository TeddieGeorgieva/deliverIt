package com.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddressDto {

    @NotNull(message = "City cannot be blank")
    private int cityId;

    @NotBlank(message = "Street cannot be blank")
    private String streetName;

    public AddressDto() {
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
