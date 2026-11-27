package com.iamlearning.tasks;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceTest {
    private final TaskService taskService = new TaskService();

    @Test
    void createsAndUpdatesTaskCompletion() {
        Task created = taskService.create("Learn Playwright");

        assertThat(created.title()).isEqualTo("Learn Playwright");
        assertThat(created.completed()).isFalse();
        assertThat(taskService.updateCompletion(created.id(), true))
                .hasValueSatisfying(task -> assertThat(task.completed()).isTrue());
    }

    @Test
    void returnsEmptyWhenUpdatingAnUnknownTask() {
        assertThat(taskService.updateCompletion(UUID.randomUUID(), true)).isEmpty();
    }
}
