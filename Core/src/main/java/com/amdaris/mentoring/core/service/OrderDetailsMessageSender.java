package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.common.kafka.ExternalMessageService;
import com.amdaris.mentoring.common.model.OrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailsMessageSender {
    private final ExternalMessageService<OrderDetails> externalMessageService;

    @Value("${externalmessage.topics.payment}")
    private String topic;

    public void send(OrderDetails orderDetails) {
        externalMessageService.send(topic, orderDetails);
    }
}
