package com.deliverit.services.role;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Role;
import com.deliverit.repositories.role.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    @Override
    public Role getById(int id) {
        return roleRepository.getById(id);
    }

    @Override
    public void create(Role role) {
        boolean duplicateExists = true;
        try {
            roleRepository.getByName(role.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Role", "name", role.getName());
        }

        roleRepository.create(role);
    }

    @Override
    public void update(Role role) {
        boolean duplicateExists = true;
        try {
            roleRepository.getByName(role.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Role", "name", role.getName());
        }

        roleRepository.update(role);
    }

    @Override
    public void delete(int id) {
        roleRepository.delete(id);
    }
}
