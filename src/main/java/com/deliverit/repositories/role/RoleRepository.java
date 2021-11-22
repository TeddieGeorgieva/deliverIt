package com.deliverit.repositories.role;

import com.deliverit.models.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> getAll();

    Role getById(int id);

    Role getByName(String name);

    void create(Role role);

    void update(Role role);

    void delete(int id);
}
