package com.deliverit.controllers.rest;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.models.AddressDto;
import com.deliverit.models.Role;
import com.deliverit.models.RoleDto;
import com.deliverit.services.role.RoleModelMapper;
import com.deliverit.services.role.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleModelMapper roleModelMapper;
    private final RoleService roleService;

    public RoleController(RoleModelMapper roleModelMapper, RoleService roleService) {
        this.roleModelMapper = roleModelMapper;
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable int id) {
        try {
            return roleService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Role create(@Valid @RequestBody RoleDto roleDto) {
        try {
            Role role = roleModelMapper.fromDto(roleDto);
            roleService.create(role);
            return role;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable int id, @Valid @RequestBody RoleDto roleDto) {
        try {
            Role role = roleModelMapper.fromDto(roleDto, id);
            roleService.update(role);
            return role;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            roleService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
