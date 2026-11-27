package com.iamlearning.modules.users.service;

import com.iamlearning.modules.users.dto.request.CreateUserRequest;
import com.iamlearning.modules.users.dto.request.UpdateUserRequest;
import com.iamlearning.modules.users.dto.response.UserResponse;
import com.iamlearning.modules.users.entity.User;
import com.iamlearning.modules.users.repository.UserRepository;
import com.iamlearning.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void returnsAllUsersAsResponses() {
        User user = User.create("Alex", "alex@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> users = userService.getAllUsers();

        assertThat(users).containsExactly(new UserResponse(user.getId(), "Alex", "alex@example.com"));
    }

    @Test
    void createsUserAndReturnsSavedUser() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse created = userService.createUser(new CreateUserRequest("  Alex  ", "  alex@example.com  "));

        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("Alex");
        assertThat(created.email()).isEqualTo("alex@example.com");
    }

    @Test
    void updatesExistingUser() {
        User user = User.create("Alex", "alex@example.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserResponse updated = userService.updateUser(
                user.getId(),
                new UpdateUserRequest("  Alex Nguyen  ", "  alex.nguyen@example.com  ")
        );

        assertThat(updated.name()).isEqualTo("Alex Nguyen");
        assertThat(updated.email()).isEqualTo("alex.nguyen@example.com");
        verify(userRepository).save(user);
    }

    @Test
    void deletesExistingUser() {
        User user = User.create("Alex", "alex@example.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository).delete(user);
    }

    @Test
    void throwsNotFoundWhenUpdatingMissingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(id, new UpdateUserRequest("Alex", "alex@example.com")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
