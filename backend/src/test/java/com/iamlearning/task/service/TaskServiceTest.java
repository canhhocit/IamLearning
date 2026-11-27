package com.iamlearning.task.service;

import com.iamlearning.modules.tasks.dto.request.CreateTaskRequest;
import com.iamlearning.modules.tasks.dto.request.UpdateTaskCompletionRequest;
import com.iamlearning.modules.tasks.dto.response.TaskResponse;
import com.iamlearning.modules.tasks.entity.Task;
import com.iamlearning.modules.tasks.event.TaskCreatedEvent;
import com.iamlearning.modules.tasks.event.TaskEventPublisher;
import com.iamlearning.modules.tasks.repository.TaskRepository;
import com.iamlearning.modules.tasks.service.TaskService;
import com.iamlearning.shared.exception.ResourceNotFoundException;

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

        TaskResponse created = taskService.createTask(new CreateTaskRequest("  Learn Playwright  "));

        assertThat(created.title()).isEqualTo("Learn Playwright");
        assertThat(created.completed()).isFalse();
        verify(taskEventPublisher).publish(any(TaskCreatedEvent.class));
    }

    @Test
    void updatesTaskCompletion() {
        Task task = Task.create("Learn Kafka");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse updated = taskService.updateTaskCompletion(task.getId(), new UpdateTaskCompletionRequest(true));

        assertThat(updated.completed()).isTrue();
        verify(taskRepository).save(task);
    }

    @Test
    void deletesExistingTask() {
        Task task = Task.create("Learn Spring Boot");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.deleteTask(task.getId());

        verify(taskRepository).delete(task);
    }

    @Test
    void throwsNotFoundExceptionWhenTaskDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTaskCompletion(id, new UpdateTaskCompletionRequest(true)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
