package com.deliverit.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ParcelDto {

    @NotNull(message = "Username can't be empty.")
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 symbols!")
    private String purchaserUsername;

    @NotNull(message = "Warehouse cannot be blank!")
    private int destinationWarehouseId;

    @Positive(message = "Weight should be more than zero!")
    private double weight;

    @NotNull(message = "Category cannot be blank!")
    private int categoryId;

    private ParcelDeliveryType deliveryType = ParcelDeliveryType.PICK_UP_FROM_WAREHOUSE;

    private int shipmentId;

    public ParcelDto() {}

    public String getPurchaserUsername() {
        return purchaserUsername;
    }

    public void setPurchaserUsername(String purchaserUsername) {
        this.purchaserUsername = purchaserUsername;
    }

    public int getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(int destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ParcelDeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(ParcelDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }
}
