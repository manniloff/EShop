package com.amdaris.mentoring.common.kafka.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicAutoConfiguration {
    @Value("${externalmessage.topics.payment}")
    private String paymentTopic;

    public NewTopic createTopic(String topicName) {
        return TopicBuilder.name(topicName)
                .build();
    }

    @Bean
    public void generateAllTopics() {
        createTopic(paymentTopic);
    }
}
