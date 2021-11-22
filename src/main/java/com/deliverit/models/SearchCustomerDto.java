package com.deliverit.models;


public class SearchCustomerDto {

    public String email;

    public String firstName;

    public String lastName;

    public String searchAll;

    public SearchCustomerDto() {
    }

    public SearchCustomerDto(String email, String firstName, String lastName, String searchAll) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.searchAll = searchAll;
    }

    public String getSearchAll() {
        return searchAll;
    }

    public void setSearchAll(String searchAll) {
        this.searchAll = searchAll;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
