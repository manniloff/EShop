package com.amdaris.mentoring.common.kafka.order;


import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.util.json.JsonCustomDeserializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class OrderDetailsKafkaConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${externalmessage.topics.payment}")
    private String paymentTopic;

    public NewTopic createTopic(String topicName) {
        return TopicBuilder.name(topicName).build();
    }

    @Bean
    public void generateAllTopics() {
        createTopic(paymentTopic);
    }

    @Bean
    public ConsumerFactory<String, OrderDetails> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonCustomDeserializer.class);
        config.put(JsonCustomDeserializer.VALUE_CLASS_NAME_CONFIG, OrderDetails.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderDetails>> factory(ConsumerFactory<String, OrderDetails> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDetails> kafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, OrderDetails> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderDetails> kafkaTemplate(ProducerFactory<String, OrderDetails> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
