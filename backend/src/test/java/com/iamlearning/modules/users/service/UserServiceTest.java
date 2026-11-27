package com.iamlearning.modules.users.service;

import com.iamlearning.modules.users.dto.request.CreateUserRequest;
import com.iamlearning.modules.users.dto.response.UserResponse;
import com.iamlearning.modules.users.entity.User;
import com.iamlearning.modules.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
    void createsUserAndReturnsSavedUser() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse created = userService.createUser(new CreateUserRequest("  Alex  ", "  alex@example.com  "));

        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("Alex");
        assertThat(created.email()).isEqualTo("alex@example.com");
    }
}
