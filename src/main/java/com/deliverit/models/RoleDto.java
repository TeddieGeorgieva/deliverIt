package com.deliverit.models;

import javax.validation.constraints.NotBlank;

public class RoleDto {

    @NotBlank(message = "Role cannot be blank")
    private String roleName;

    public RoleDto() {
    }

    public RoleDto(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return roleName;
    }

    public void setName(String roleName) {
        this.roleName = roleName;
    }
}
