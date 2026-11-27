package com.iamlearning.modules.users.service;

import com.iamlearning.modules.users.dto.request.CreateUserRequest;
import com.iamlearning.modules.users.dto.request.UpdateUserRequest;
import com.iamlearning.modules.users.dto.response.UserResponse;
import com.iamlearning.modules.users.entity.User;
import com.iamlearning.modules.users.repository.UserRepository;
import com.iamlearning.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = User.create(request.name().trim(), request.email().trim());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = findUserOrThrow(id);
        user.updateProfile(request.name().trim(), request.email().trim());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.delete(findUserOrThrow(id));
    }

    private User findUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
