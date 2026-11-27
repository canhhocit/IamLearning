package com.iamlearning.tasks.api;

import com.iamlearning.tasks.api.dto.CreateTaskRequest;
import com.iamlearning.tasks.api.dto.SetTaskCompletionRequest;
import com.iamlearning.tasks.api.dto.TaskResponse;
import com.iamlearning.tasks.application.TaskView;
import com.iamlearning.tasks.application.TaskUseCases;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskUseCases taskUseCases;

    public TaskController(TaskUseCases taskUseCases) {
        this.taskUseCases = taskUseCases;
    }

    @GetMapping
    public List<TaskResponse> findAll() {
        return taskUseCases.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        TaskView task = taskUseCases.create(request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(task));
    }

    @PatchMapping("/{id}")
    public TaskResponse updateCompletion(
            @PathVariable UUID id,
            @RequestBody SetTaskCompletionRequest request
    ) {
        return taskUseCases.updateCompletion(id, request.completed())
                .map(TaskResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!taskUseCases.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return ResponseEntity.noContent().build();
    }
}
