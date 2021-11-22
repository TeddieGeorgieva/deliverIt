package com.deliverit.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserDto {

    @NotEmpty
    private String username;

    @NotEmpty(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 symbols.")
    private String firstName;

    @NotEmpty(message = "Name can't be empty")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols.")
    private String lastName;

    @NotEmpty
    private String email;

    @NotNull
    private int roleId;

    public UserDto(String username, String firstName, String lastName, String email, int roleId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getRole() {
        return roleId;
    }

    public void setRole(int role) {
        this.roleId = role;
    }

}
