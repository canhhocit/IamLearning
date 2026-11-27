package com.iamlearning.tasks.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(UUID id);

    Task save(Task task);

    void delete(Task task);
}
