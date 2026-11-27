package com.iamlearning.tasks.events;

public interface TaskEventPublisher {
    void publish(TaskCreatedEvent event);
}
