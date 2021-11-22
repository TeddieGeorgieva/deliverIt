package com.deliverit.models;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "street_name")
    private String streetName;

    public Address() {
    }

    public Address(int id, City city, String streetName) {
        this.id = id;
        this.city = city;
        this.streetName = streetName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    //todo: can i override toString?

    @Override
    public String toString(){
        return String.format("%s%n, %s%n, %s", getCity().getCountry().getName(), getCity().getName(), getStreetName());
    }
}
