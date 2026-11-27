package com.iamlearning.tasks;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {
    private final ConcurrentHashMap<UUID, Task> tasks = new ConcurrentHashMap<>();

    public List<Task> findAll() {
        return List.copyOf(tasks.values());
    }

    public Task create(String title) {
        Task task = new Task(UUID.randomUUID(), title.trim(), false);
        tasks.put(task.id(), task);
        return task;
    }

    public Optional<Task> updateCompletion(UUID id, boolean completed) {
        return Optional.ofNullable(tasks.computeIfPresent(
                id,
                (taskId, task) -> new Task(task.id(), task.title(), completed)
        ));
    }

    public boolean delete(UUID id) {
        return tasks.remove(id) != null;
    }
}
