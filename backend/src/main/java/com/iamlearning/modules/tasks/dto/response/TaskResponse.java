package com.iamlearning.modules.tasks.dto.response;

import java.util.UUID;

import com.iamlearning.modules.tasks.entity.Task;

public record TaskResponse(UUID id, String title, boolean completed) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.isCompleted());
    }
}
