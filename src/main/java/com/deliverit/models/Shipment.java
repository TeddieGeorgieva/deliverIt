package com.deliverit.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "origin_warehouse_id")
    private Warehouse originWarehouse;

    @ManyToOne
    @JoinColumn(name = "destination_warehouse_id")
    private Warehouse destinationWarehouse;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "shipments_parcels",
            joinColumns = { @JoinColumn(name = "shipment_id") },
            inverseJoinColumns = { @JoinColumn(name = "parcel_id") }
    )
    private Set<Parcel> parcels;

    @Column(name = "status")
    private Status status;

    public Shipment(Warehouse originWarehouse, Warehouse destinationWarehouse, LocalDate departureDate, LocalDate arrivalDate, Set<Parcel> parcels) {
        this.originWarehouse = originWarehouse;
        this.destinationWarehouse = destinationWarehouse;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.parcels = parcels;
        this.status = Status.PREPARING;
    }

    public Shipment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Warehouse getOriginWarehouse() {
        return originWarehouse;
    }

    public void setOriginWarehouse(Warehouse originWarehouse) {
        this.originWarehouse = originWarehouse;
    }

    public Warehouse getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(Warehouse destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
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

    public Set<Parcel> getParcels() {
        return parcels;
    }

    public void setParcels(Set<Parcel> parcels) {
        this.parcels = parcels;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void changeStatus() {
        LocalDate today = LocalDate.now();
        boolean onTheWay = Math.abs(today.getDayOfYear() - departureDate.getDayOfYear()) > 0;
        boolean completed = Math.abs(today.getDayOfYear() - arrivalDate.getDayOfYear()) >= 0;
        if (onTheWay) {
            this.status = Status.ON_THE_WAY;
        } else if (completed) {
            this.status = Status.COMPLETED;
        } else {
            this.status = Status.PREPARING;
        }
    }

    public void addParcel(Parcel parcel) {
        this.parcels.add(parcel);
    }

    public void removeParcel(Parcel parcel) {
        this.parcels.remove(parcel);
    }
}
