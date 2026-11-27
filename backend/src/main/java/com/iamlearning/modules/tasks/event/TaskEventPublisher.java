package com.iamlearning.modules.tasks.event;

public interface TaskEventPublisher {
    void publish(TaskCreatedEvent event);
}
