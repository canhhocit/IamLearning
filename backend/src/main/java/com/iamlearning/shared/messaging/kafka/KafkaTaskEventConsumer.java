package com.iamlearning.shared.messaging.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.iamlearning.modules.tasks.event.TaskCreatedEvent;

@Component
public class KafkaTaskEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTaskEventConsumer.class);

    @KafkaListener(topics = KafkaTopics.TASK_CREATED)
    public void onTaskCreated(TaskCreatedEvent event) {
        logger.info("Consumed {} event: taskId={}, title={}", KafkaTopics.TASK_CREATED, event.taskId(), event.title());
    }
}
