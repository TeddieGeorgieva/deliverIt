package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Role;
import com.deliverit.repositories.role.RoleRepository;
import com.deliverit.services.role.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.deliverit.Helpers.createMockCountry;
import static com.deliverit.Helpers.createMockRole;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    RoleRepository mockRepository;

    @InjectMocks
    RoleServiceImpl service;

    @Test
    public void getAll_Should_Return_AllRoles() {
        var role = createMockRole();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(role));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_Role_WhenMatchExists() {
        var role = createMockRole();

        Mockito.when(mockRepository.getById(role.getId()))
                .thenReturn(role);

        Assertions.assertDoesNotThrow(() -> service.getById(role.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(role.getId());
    }
    @Test
    public void create_Should_Throw_When_RoleNameIsTaken() {
        var role = createMockRole();
        Mockito.when(mockRepository.getByName(role.getName()))
                .thenReturn(role);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(role));
    }

    @Test
    public void create_ShouldCallRepository_When_RoleDoesNotExists() {
        var role = createMockRole();

        Mockito.when(mockRepository.getByName(role.getName()))
                .thenThrow(new EntityNotFoundException("Role", "name", role.getName()));

        Assertions.assertDoesNotThrow(() -> service.create(role));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Role.class));
    }


    @Test
    public void update_Should_Throw_When_RoleNameIsTaken() {
        var role = createMockRole();

        Mockito.when(mockRepository.getByName(role.getName()))
                .thenReturn(role);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(role));
    }

    @Test
    public void update_Should_Throw_When_RoleNameDoesNotExists() {
        var role = createMockRole();

        Mockito.when(mockRepository.getByName(role.getName()))
                .thenThrow(new EntityNotFoundException("Role", "name", role.getName()));

        Assertions.assertDoesNotThrow(() -> service.update(role));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Role.class));
    }
    @Test
    public void delete_Should_Delete_Role() {
        var role = createMockRole();

        Assertions.assertDoesNotThrow(() -> service.delete(role.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(role.getId());
    }
}
