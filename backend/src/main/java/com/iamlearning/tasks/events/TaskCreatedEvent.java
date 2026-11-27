package com.iamlearning.tasks.events;

import com.iamlearning.tasks.entities.Task;

import java.time.Instant;
import java.util.UUID;

public record TaskCreatedEvent(UUID taskId, String title, Instant createdAt) {
    public static TaskCreatedEvent from(Task task) {
        return new TaskCreatedEvent(task.getId(), task.getTitle(), Instant.now());
    }
}
