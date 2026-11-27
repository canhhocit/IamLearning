package com.iamlearning.tasks.dto.response;

import com.iamlearning.tasks.entities.Task;

import java.util.UUID;

public record TaskResponse(UUID id, String title, boolean completed) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.isCompleted());
    }
}
