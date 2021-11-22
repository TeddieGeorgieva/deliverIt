package com.deliverit.models;

public class ParcelCustomerDto {

    private Integer deliveryType;

    public ParcelCustomerDto() {
    }

    public ParcelCustomerDto(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }
}
