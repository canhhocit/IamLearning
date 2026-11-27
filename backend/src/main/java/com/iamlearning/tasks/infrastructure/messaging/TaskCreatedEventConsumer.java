package com.iamlearning.tasks.infrastructure.messaging;

import com.iamlearning.tasks.domain.event.TaskCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TaskCreatedEventConsumer.class);

    @KafkaListener(topics = KafkaTaskEventPublisher.TASK_CREATED_TOPIC)
    public void onTaskCreated(TaskCreatedEvent event) {
        logger.info("Consumed task.created event: taskId={}, title={}", event.taskId(), event.title());
    }
}
