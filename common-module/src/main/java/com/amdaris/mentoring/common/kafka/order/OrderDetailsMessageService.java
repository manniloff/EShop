package com.amdaris.mentoring.common.kafka.order;

import com.amdaris.mentoring.common.kafka.ExternalMessageService;
import com.amdaris.mentoring.common.kafka.order.config.KafkaOrderDetailsProducerAutoConfiguration;
import com.amdaris.mentoring.common.model.OrderDetails;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Import(KafkaOrderDetailsProducerAutoConfiguration.class)
public class OrderDetailsMessageService implements ExternalMessageService<OrderDetails> {
    @Autowired
    KafkaOrderDetailsProducerAutoConfiguration kafkaOrderDetailsProducerAutoConfiguration;

    private final Map<String, List<ExternalMessageService.ReceiveListener>> listeners = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${externalmessage.topics.payment}", groupId = "${kafka.group.id}")
    public void consume(ConsumerRecord<String, Object> message) {
        String topic = message.topic();

        if (!listeners.containsKey(topic)) {
            return;
        }

        for (ExternalMessageService.ReceiveListener receiveListener : listeners.get(topic)) {
            receiveListener.onReceive(topic, message.value());
        }
    }

    public void send(String topic, OrderDetails message) {
        ProducerFactory<String, OrderDetails> orderDetailsProducerFactory
                = kafkaOrderDetailsProducerAutoConfiguration.producerFactory();

        KafkaTemplate<String, OrderDetails> orderDetailsKafkaTemplate =
                kafkaOrderDetailsProducerAutoConfiguration.kafkaTemplate(orderDetailsProducerFactory);
        orderDetailsKafkaTemplate.send(topic, message);
    }

    public void addReceiveListener(String topic, ExternalMessageService.ReceiveListener listener) {
        if (!listeners.containsKey(topic)) {
            listeners.put(topic, new CopyOnWriteArrayList<>());
        }

        listeners.get(topic).add(listener);
    }
}
