package com.amdaris.mentoring.payment.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaExternalMessageService implements ExternalMessageService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Map<String, List<ReceiveListener>> listeners = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Kafka service started");
    }

    @KafkaListener(topics = "#{'${kafka.topics}'.split(',')}", groupId = "${kafka.group.id}")
    public void consume(ConsumerRecord<String, String> message) {
        String topic = message.topic();

        log.info("Consumed Kafka message from topic: {}, message content: {}", topic, message);

        if (!listeners.containsKey(topic)) {
            return;
        }

        for (ReceiveListener receiveListener : listeners.get(topic)) {
            receiveListener.onReceive(topic, message.value());
        }
    }

    public void send(String topic, String message) {
        log.info("Produce Kafka message to topic: {}, message content: {}", topic, message);

        kafkaTemplate.send(topic, message);
    }

    @Override
    public void addReceiveListener(String topic, ReceiveListener listener) {
        if (!listeners.containsKey(topic)) {
            listeners.put(topic, new CopyOnWriteArrayList<>());
        }

        listeners.get(topic).add(listener);
    }
}
