package com.iamlearning.tasks.api.dto;

import com.iamlearning.tasks.application.TaskView;

import java.util.UUID;

public record TaskResponse(UUID id, String title, boolean completed) {
    public static TaskResponse from(TaskView task) {
        return new TaskResponse(task.id(), task.title(), task.completed());
    }
}
