package com.amdaris.mentoring.payment.kafka;

import com.amdaris.mentoring.common.model.OrderDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailsKafkaConsumer {
    @KafkaListener(topics = "${externalmessage.topics.payment}", groupId = "${kafka.group.id}",
            containerFactory = "kafkaListenerFactory")
    public void consume(OrderDetails orderDetails) {
        log.info("Kafka message - " + orderDetails.toString());
    }
}