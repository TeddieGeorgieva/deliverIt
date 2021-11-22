package com.deliverit.models;


public class SearchParcelDto {

    public Integer customerId;

    public Integer destinationWarehouseId;

    public Double weight;

    public Integer categoryId;

    public String searchAll;

    public SearchParcelDto() {
    }

    public SearchParcelDto(Integer customerId, Integer destinationWarehouseId, Double weight, Integer categoryId, String searchAll) {
        this.customerId = customerId;
        this.destinationWarehouseId = destinationWarehouseId;
        this.weight = weight;
        this.categoryId = categoryId;
        this.searchAll = searchAll;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(Integer destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSearchAll() {
        return searchAll;
    }

    public void setSearchAll(String searchAll) {
        this.searchAll = searchAll;
    }
}
