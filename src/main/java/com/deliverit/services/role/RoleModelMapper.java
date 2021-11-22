package com.deliverit.services.role;

import com.deliverit.models.*;
import com.deliverit.repositories.role.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleModelMapper {

    private final RoleRepository roleRepository;

    public RoleModelMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role fromDto(RoleDto roleDto) {
        Role role = new Role();
        dtoToObject(roleDto, role);
        return role;
    }

    public Role fromDto(RoleDto roleDto, int id) {
        Role role = roleRepository.getById(id);
        dtoToObject(roleDto, role);
        return role;
    }

    private void dtoToObject(RoleDto roleDto, Role role) {
        role.setName(roleDto.getName());
    }
}
