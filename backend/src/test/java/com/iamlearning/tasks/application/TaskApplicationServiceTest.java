package com.iamlearning.tasks.application;

import com.iamlearning.tasks.domain.Task;
import com.iamlearning.tasks.domain.TaskRepository;
import com.iamlearning.tasks.domain.event.TaskCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskApplicationServiceTest {
    private TaskRepository taskRepository;
    private TaskEventPublisher eventPublisher;
    private TaskApplicationService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        eventPublisher = mock(TaskEventPublisher.class);
        taskService = new TaskApplicationService(taskRepository, eventPublisher);
    }

    @Test
    void createsTaskAndPublishesDomainEvent() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskView created = taskService.create("  Learn Playwright  ");

        assertThat(created.title()).isEqualTo("Learn Playwright");
        assertThat(created.completed()).isFalse();
        verify(eventPublisher).publish(any(TaskCreatedEvent.class));
    }

    @Test
    void updatesCompletionAndReturnsEmptyForUnknownTask() {
        Task task = Task.create("Learn Kafka");
        when(taskRepository.findById(task.id())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(taskService.updateCompletion(task.id(), true))
                .hasValueSatisfying(updated -> assertThat(updated.completed()).isTrue());
        verify(taskRepository).save(new Task(task.id(), task.title(), true));

        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThat(taskService.updateCompletion(UUID.randomUUID(), true)).isEmpty();
        verify(taskRepository, times(2)).findById(any(UUID.class));
    }
}
