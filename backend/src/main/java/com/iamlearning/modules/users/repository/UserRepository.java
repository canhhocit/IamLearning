package com.iamlearning.modules.users.repository;

import com.iamlearning.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
