package com.iamlearning.tasks.infrastructure.persistence;

import com.iamlearning.tasks.domain.Task;
import com.iamlearning.tasks.domain.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaTaskRepositoryAdapter implements TaskRepository {
    private final SpringDataTaskRepository springDataRepository;

    public JpaTaskRepositoryAdapter(SpringDataTaskRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<Task> findAll() {
        return springDataRepository.findAll().stream()
                .map(TaskEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return springDataRepository.findById(id)
                .map(TaskEntity::toDomain);
    }

    @Override
    public Task save(Task task) {
        return springDataRepository.save(TaskEntity.fromDomain(task))
                .toDomain();
    }

    @Override
    public void delete(Task task) {
        springDataRepository.deleteById(task.id());
    }
}
