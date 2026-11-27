package com.iamlearning.tasks.services;

import com.iamlearning.shared.exceptions.ResourceNotFoundException;
import com.iamlearning.tasks.entities.Task;
import com.iamlearning.tasks.events.TaskCreatedEvent;
import com.iamlearning.tasks.events.TaskEventPublisher;
import com.iamlearning.tasks.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    public TaskService(TaskRepository taskRepository, TaskEventPublisher taskEventPublisher) {
        this.taskRepository = taskRepository;
        this.taskEventPublisher = taskEventPublisher;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task create(String title) {
        Task task = taskRepository.save(Task.create(title.trim()));
        taskEventPublisher.publish(TaskCreatedEvent.from(task));
        return task;
    }

    @Transactional
    public Task updateCompletion(UUID id, boolean completed) {
        Task task = findTaskOrThrow(id);
        task.setCompleted(completed);
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(UUID id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    private Task findTaskOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }
}
