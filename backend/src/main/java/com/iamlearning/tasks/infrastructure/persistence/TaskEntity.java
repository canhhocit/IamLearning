package com.iamlearning.tasks.infrastructure.persistence;

import com.iamlearning.tasks.domain.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private boolean completed;

    protected TaskEntity() {
    }

    static TaskEntity fromDomain(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.id = task.id();
        entity.title = task.title();
        entity.completed = task.completed();
        return entity;
    }

    Task toDomain() {
        return new Task(id, title, completed);
    }
}
