package com.iamlearning.tasks.domain;

import java.util.UUID;

public record Task(UUID id, String title, boolean completed) {
    public static Task create(String title) {
        return new Task(UUID.randomUUID(), title, false);
    }

    public Task withCompletion(boolean completed) {
        return new Task(id, title, completed);
    }
}
