package com.iamlearning.tasks.services;

import com.iamlearning.shared.exceptions.ResourceNotFoundException;
import com.iamlearning.tasks.entities.Task;
import com.iamlearning.tasks.events.TaskCreatedEvent;
import com.iamlearning.tasks.events.TaskEventPublisher;
import com.iamlearning.tasks.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    private TaskRepository taskRepository;
    private TaskEventPublisher taskEventPublisher;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskEventPublisher = mock(TaskEventPublisher.class);
        taskService = new TaskService(taskRepository, taskEventPublisher);
    }

    @Test
    void createsTaskAndPublishesEvent() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task created = taskService.create("  Learn Playwright  ");

        assertThat(created.getTitle()).isEqualTo("Learn Playwright");
        assertThat(created.isCompleted()).isFalse();
        verify(taskEventPublisher).publish(any(TaskCreatedEvent.class));
    }

    @Test
    void updatesTaskCompletion() {
        Task task = Task.create("Learn Kafka");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updated = taskService.updateCompletion(task.getId(), true);

        assertThat(updated.isCompleted()).isTrue();
        verify(taskRepository).save(task);
    }

    @Test
    void throwsNotFoundExceptionWhenTaskDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateCompletion(id, true))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
