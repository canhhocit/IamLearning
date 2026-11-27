package com.iamlearning.modules.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iamlearning.modules.tasks.entity.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
