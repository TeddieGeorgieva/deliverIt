package com.deliverit.services.users;

import com.deliverit.models.*;
import com.deliverit.repositories.address.AddressRepository;
import com.deliverit.repositories.role.RoleRepository;
import com.deliverit.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserModelMapper {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public UserModelMapper(UserRepository userRepository, RoleRepository roleRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, int id) {
        User user = userRepository.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) {
        user.setRole(roleRepository.getById(userDto.getRole()));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setAddressForDelivery(addressRepository.getById(1));

        Role role = new Role();
        role.setId(1);
        role.setName("customer");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }
}
