package com.iamlearning.tasks.application;

import com.iamlearning.tasks.domain.Task;
import com.iamlearning.tasks.domain.TaskRepository;
import com.iamlearning.tasks.domain.event.TaskCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskApplicationService implements TaskUseCases {
    private final TaskRepository taskRepository;
    private final TaskEventPublisher eventPublisher;

    public TaskApplicationService(
            TaskRepository taskRepository,
            TaskEventPublisher eventPublisher
    ) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<TaskView> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskView::from)
                .toList();
    }

    @Override
    public TaskView create(String title) {
        Task task = taskRepository.save(Task.create(title.trim()));
        eventPublisher.publish(TaskCreatedEvent.from(task));
        return TaskView.from(task);
    }

    @Override
    public Optional<TaskView> updateCompletion(UUID id, boolean completed) {
        return taskRepository.findById(id)
                .map(task -> TaskView.from(taskRepository.save(task.withCompletion(completed))));
    }

    @Override
    public boolean delete(UUID id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElse(false);
    }
}
