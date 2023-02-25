package com.amdaris.mentoring.common.kafka.order.config;

import com.amdaris.mentoring.common.kafka.ExternalMessageService;
import com.amdaris.mentoring.common.kafka.order.OrderDetailsMessageService;
import com.amdaris.mentoring.common.model.OrderDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDetailsMessageServiceAutoConfiguration {
    @Bean
    public ExternalMessageService<OrderDetails> orderDetailsExternalMessageService() {
        return new OrderDetailsMessageService();
    }
}
