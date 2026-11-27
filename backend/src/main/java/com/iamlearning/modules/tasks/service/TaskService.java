package com.iamlearning.modules.tasks.service;

import com.iamlearning.modules.tasks.dto.request.CreateTaskRequest;
import com.iamlearning.modules.tasks.dto.request.UpdateTaskCompletionRequest;
import com.iamlearning.modules.tasks.dto.response.TaskResponse;
import com.iamlearning.modules.tasks.entity.Task;
import com.iamlearning.modules.tasks.event.TaskCreatedEvent;
import com.iamlearning.modules.tasks.event.TaskEventPublisher;
import com.iamlearning.modules.tasks.repository.TaskRepository;
import com.iamlearning.shared.exception.ResourceNotFoundException;

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

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = taskRepository.save(Task.create(request.title().trim()));
        taskEventPublisher.publish(TaskCreatedEvent.from(task));
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse updateTaskCompletion(UUID id, UpdateTaskCompletionRequest request) {
        Task task = findTaskOrThrow(id);
        task.setCompleted(request.completed());
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(UUID id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    private Task findTaskOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }
}
