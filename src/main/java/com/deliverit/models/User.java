package com.deliverit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private static final String EMPLOYEE_ROLE = "employee";
    private static final String CUSTOMER_ROLE = "customer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @javax.validation.constraints.Email(regexp = ".+@.+\\..+")
    private String email;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address addressForDelivery;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    public User() {}

    public User(int id, String username, String password, String firstName,
                String lastName, String email, Address addressForDelivery, int isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressForDelivery = addressForDelivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddressForDelivery() {
        return addressForDelivery;
    }

    public void setAddressForDelivery(Address addressForDelivery) {
        this.addressForDelivery = addressForDelivery;
    }

    @JsonIgnore
    public boolean isEmployee() {
        return getRoles().stream().anyMatch(r -> r.getName().equals(EMPLOYEE_ROLE));
    }

    @JsonIgnore
    public boolean isCustomer() {
        return getRoles().stream().anyMatch(r -> r.getName().equals(CUSTOMER_ROLE));
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if(roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }
    public void setRole(Role role) {
            //this.role = role.getId();
            addRole(role);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
