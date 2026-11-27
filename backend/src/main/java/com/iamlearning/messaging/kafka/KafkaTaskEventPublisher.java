package com.iamlearning.messaging.kafka;

import com.iamlearning.tasks.events.TaskCreatedEvent;
import com.iamlearning.tasks.events.TaskEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTaskEventPublisher implements TaskEventPublisher {
    static final String TASK_CREATED_TOPIC = "task.created";

    private static final Logger logger = LoggerFactory.getLogger(KafkaTaskEventPublisher.class);
    private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

    public KafkaTaskEventPublisher(KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TaskCreatedEvent event) {
        kafkaTemplate.send(TASK_CREATED_TOPIC, event.taskId().toString(), event)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        logger.error("Failed to publish task.created event for task {}", event.taskId(), error);
                    }
                });
    }
}
