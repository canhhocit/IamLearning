package com.iamlearning.tasks.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskUseCases {
    List<TaskView> findAll();

    TaskView create(String title);

    Optional<TaskView> updateCompletion(UUID id, boolean completed);

    boolean delete(UUID id);
}
