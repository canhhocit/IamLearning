package com.iamlearning.shared.messaging.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.iamlearning.modules.tasks.event.TaskCreatedEvent;
import com.iamlearning.modules.tasks.event.TaskEventPublisher;

@Component
public class KafkaTaskEventPublisher implements TaskEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTaskEventPublisher.class);
    private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

    public KafkaTaskEventPublisher(KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(TaskCreatedEvent event) {
        kafkaTemplate.send(KafkaTopics.TASK_CREATED, event.taskId().toString(), event)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        logger.error("Failed to publish {} event for task {}", KafkaTopics.TASK_CREATED, event.taskId(), error);
                    }
                });
    }
}
