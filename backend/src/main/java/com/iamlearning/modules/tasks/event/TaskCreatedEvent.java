package com.iamlearning.modules.tasks.event;

import java.time.Instant;
import java.util.UUID;

import com.iamlearning.modules.tasks.entity.Task;

public record TaskCreatedEvent(UUID taskId, String title, Instant createdAt) {
    public static TaskCreatedEvent from(Task task) {
        return new TaskCreatedEvent(task.getId(), task.getTitle(), Instant.now());
    }
}
