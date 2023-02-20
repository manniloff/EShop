package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.core.model.OrderDetails;
import com.amdaris.mentoring.core.util.json.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final JsonService jsonService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${externalmessage.topics.payment}")
    private String topic;

    @PostMapping(value = "/send/to/pay")
    public ResponseEntity<?> sendOrderToPay(@RequestBody OrderDetails orderDetails) {
        kafkaTemplate.send(topic, jsonService.toJson(orderDetails));
        return ResponseEntity.ok("message was sent to kafka");
    }
}
