package com.iamlearning.tasks;

import java.time.Instant;
import java.util.UUID;

public record TaskCreatedEvent(UUID taskId, String title, Instant createdAt) {
    public static TaskCreatedEvent from(Task task) {
        return new TaskCreatedEvent(task.id(), task.title(), Instant.now());
    }
}
