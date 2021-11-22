package com.deliverit.services.role;
import com.deliverit.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role getById(int id);

    void create(Role role);

    void update(Role role);

    void delete(int id);
}
