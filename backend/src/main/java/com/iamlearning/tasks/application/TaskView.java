package com.iamlearning.tasks.application;

import com.iamlearning.tasks.domain.Task;

import java.util.UUID;

public record TaskView(UUID id, String title, boolean completed) {
    public static TaskView from(Task task) {
        return new TaskView(task.id(), task.title(), task.completed());
    }
}
