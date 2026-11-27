package com.iamlearning.tasks.domain.event;

import com.iamlearning.tasks.domain.Task;

import java.time.Instant;
import java.util.UUID;

public record TaskCreatedEvent(UUID taskId, String title, Instant createdAt) {
    public static TaskCreatedEvent from(Task task) {
        return new TaskCreatedEvent(task.id(), task.title(), Instant.now());
    }
}
