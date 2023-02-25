package com.amdaris.mentoring.payment.kafka;

import com.amdaris.mentoring.common.kafka.ExternalMessageService;
import com.amdaris.mentoring.common.model.OrderDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalOrderDetailsMessageService implements ExternalMessageService.ReceiveListener {
    private final ExternalMessageService externalMessageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${externalmessage.topics.payment}")
    private String topic;

    @PostConstruct
    public void init() {
        externalMessageService.addReceiveListener(topic, this);
    }


    @Override
    public void onReceive(String topic, Object message) {
        try {
            OrderDetails orderDetails = objectMapper.readValue(message.toString(), OrderDetails.class);
            log.info("Consume - {}", orderDetails);
        } catch (Exception ex) {
            log.warn("Event handling error, processing by subsequent listeners skipped {}", ex.getMessage(), ex);
        }
    }
}
