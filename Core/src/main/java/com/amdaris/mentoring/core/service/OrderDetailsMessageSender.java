package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.common.model.OrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailsMessageSender implements KafkaMessageSender<OrderDetails>{
    private final KafkaTemplate<String, OrderDetails> kafkaTemplate;
    @Value("${externalmessage.topics.payment}")
    private String topic;

    public void send(OrderDetails orderDetails) {
        kafkaTemplate.send(topic, orderDetails);
    }
}
