package com.iamlearning.modules.users.service;

import com.iamlearning.modules.users.dto.request.CreateUserRequest;
import com.iamlearning.modules.users.dto.response.UserResponse;
import com.iamlearning.modules.users.entity.User;
import com.iamlearning.modules.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = User.create(request.name().trim(), request.email().trim());
        return UserResponse.from(userRepository.save(user));
    }
}
