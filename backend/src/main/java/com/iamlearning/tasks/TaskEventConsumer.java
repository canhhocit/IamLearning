package com.iamlearning.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TaskEventConsumer.class);

    @KafkaListener(topics = TaskEventPublisher.TASK_CREATED_TOPIC)
    public void onTaskCreated(TaskCreatedEvent event) {
        logger.info("Consumed task.created event: taskId={}, title={}", event.taskId(), event.title());
    }
}
