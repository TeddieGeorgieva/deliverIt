package com.deliverit.services.users;

import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> search(Optional<String> search);

    int getNumberOfUsers();

    List<User> filter(String firstName, String lastName, String email, String searchAll);
}
