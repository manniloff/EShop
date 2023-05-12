package com.amdaris.mentoring.payment.kafka;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.payment.env.ExternalMessageTestConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ExternalMessageTestConfiguration.class)
@SpringBootTest
public class OrderDetailsKafkaConsumerTests {
    @Autowired
    private ExternalMessageTestConfiguration externalMessageTestConfiguration;

    @Autowired
    private OrderDetailsKafkaConsumer orderDetailsKafkaConsumer;

    @Value("${externalmessage.topics.payment}")
    private String topic;

    @BeforeEach
    public void beforeEach() {
        externalMessageTestConfiguration.reset();
    }

    @DisplayName("Test that message was consumed by consumer")
    @Test
    public void consumeKafkaMessage_dataIsValid_returnConsumedMessages() throws JsonProcessingException {
        ProductDetails firstProductDetails = new ProductDetails();
        firstProductDetails.setTitle("Product1");
        firstProductDetails.setCount(2);
        firstProductDetails.setPrice(20.0);

        ProductDetails secondProductDetails = new ProductDetails();
        secondProductDetails.setTitle("Product2");
        secondProductDetails.setCount(1);
        secondProductDetails.setPrice(30.0);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);

        externalMessageTestConfiguration.externalMessageService().send(topic, orderDetails);


        orderDetailsKafkaConsumer.consume(orderDetails);
    }
}
