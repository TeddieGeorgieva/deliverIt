package com.deliverit.models;

public class SearchShipmentDto {

    public Integer originWarehouseId;

    public Integer destinationWarehouseId;

    public Integer customerId;

    public SearchShipmentDto() {
    }

    public SearchShipmentDto(Integer originWarehouseId, Integer destinationWarehouseId, Integer customerId) {
        this.originWarehouseId = originWarehouseId;
        this.destinationWarehouseId = destinationWarehouseId;
        this.customerId = customerId;
    }

    public Integer getOriginWarehouseId() {
        return originWarehouseId;
    }

    public void setOriginWarehouseId(Integer originWarehouseId) {
        this.originWarehouseId = originWarehouseId;
    }

    public Integer getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(Integer destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
