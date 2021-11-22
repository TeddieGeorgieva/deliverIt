package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.User;
import com.deliverit.repositories.users.UserRepository;
import com.deliverit.services.users.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getAll_Should_Return_AllUsers() {
        var user = createMockUser();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(user));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getById_Should_Return_User_WhenMatchExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);

        Assertions.assertDoesNotThrow(() -> service.getById(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(user.getId());
    }
    @Test
    public void getByUsername_Should_Return_User_WhenMatchExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenReturn(user);

        Assertions.assertDoesNotThrow(() -> service.getByUsername(user.getUsername()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByUsername(user.getUsername());
    }
    @Test
    public void create_Should_Throw_When_UserNameIsTaken() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenReturn(user);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_ShouldCallRepository_When_UserDoesNotExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "name", user.getEmail()));

        Assertions.assertDoesNotThrow(() -> service.create(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(User.class));
    }


    @Test
    public void update_Should_Throw_When_UserNameIsTaken() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenReturn(user);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(user));
    }

    @Test
    public void update_Should_Throw_When_UserNameDoesNotExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "name", user.getEmail()));

        Assertions.assertDoesNotThrow(() -> service.update(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }
    @Test
    public void delete_Should_Delete_User() {
        var user = createMockUser();

        Assertions.assertDoesNotThrow(() -> service.delete(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(user.getId());
    }

    @Test
    public void search_Should_Return_AllUsers_When_SearchIsEmpty() {

        Assertions.assertDoesNotThrow(() -> service.search(Optional.empty()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
//    @Test
//    public void search_Should_Return_ConcreteUsers_When_SearchIsNotEmpty() {
//
//        Optional<String> search = Optional.of("username");
//        Assertions.assertDoesNotThrow(() -> service.search(Optional.empty()));
//
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .search(search);
//    }
}
