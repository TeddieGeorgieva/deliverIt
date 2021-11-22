package com.deliverit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "parcels")
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User purchaser;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse destinationWarehouse;

    @Column(name = "weight")
    private double weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "delivery_type")
    private ParcelDeliveryType deliveryType;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "parcels")
    private List<Shipment> shipments;

    public Parcel() {
    }

    public Parcel(int id, User purchaser, Warehouse destinationWarehouse, double weight, Category category, ParcelDeliveryType deliveryType) {
        this.id = id;
        this.purchaser = purchaser;
        this.destinationWarehouse = destinationWarehouse;
        this.weight = weight;
        this.category = category;
        this.deliveryType = deliveryType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(User purchaser) {
        this.purchaser = purchaser;
    }

    public Warehouse getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(Warehouse destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ParcelDeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(ParcelDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return id == parcel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
