package com.deliverit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ShipmentDto {

    @NotNull
    private int originWarehouseId;

    @NotNull
    private int destinationWarehouseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    private Status status = Status.PREPARING;

    private Set<Parcel> parcels;

    public ShipmentDto() { }

    public int getOriginWarehouseId() {
        return originWarehouseId;
    }

    public void setOriginWarehouseId(int originWarehouseId) {
        this.originWarehouseId = originWarehouseId;
    }

    public int getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(int destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setParcels(Set<Parcel> parcels) {
        this.parcels = parcels;
    }
    public Set<Parcel> getParcels() {
        return parcels;
    }
}
