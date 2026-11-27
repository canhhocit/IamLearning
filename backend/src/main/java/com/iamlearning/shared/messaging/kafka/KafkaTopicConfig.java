package com.iamlearning.shared.messaging.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    NewTopic taskCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.TASK_CREATED)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
