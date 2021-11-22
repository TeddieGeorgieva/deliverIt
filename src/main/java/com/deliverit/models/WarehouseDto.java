package com.deliverit.models;
import javax.validation.constraints.NotNull;

public class WarehouseDto {

    @NotNull(message = "Warehouse address cannot be blank.")
    private int addressId;

    public WarehouseDto() {
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
