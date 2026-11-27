package com.iamlearning.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskEventPublisher {
    public static final String TASK_CREATED_TOPIC = "task.created";

    private static final Logger logger = LoggerFactory.getLogger(TaskEventPublisher.class);
    private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

    public TaskEventPublisher(KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCreated(Task task) {
        TaskCreatedEvent event = TaskCreatedEvent.from(task);
        kafkaTemplate.send(TASK_CREATED_TOPIC, task.id().toString(), event)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        logger.error("Failed to publish task.created event for task {}", task.id(), error);
                    }
                });
    }
}
