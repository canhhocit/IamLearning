package com.iamlearning.tasks.application;

import com.iamlearning.tasks.domain.event.TaskCreatedEvent;

public interface TaskEventPublisher {
    void publish(TaskCreatedEvent event);
}
