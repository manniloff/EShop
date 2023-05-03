package com.amdaris.mentoring.core.service;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.core.env.KafkaTestMessageSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OrderDetailsMessageSenderTests {
    @Autowired
    private KafkaMessageSender<Object> kafkaMessageSender;

    @Autowired
    private KafkaTestMessageSender kafkaTestMessageSender;

    @BeforeEach
    public void beforeEach() {
        kafkaTestMessageSender.reset();
    }

    @DisplayName("Test that message was sent to kafka")
    @Test
    public void testThatMessageWasSentToKafka() {
        ProductDetails firstProductDetails = new ProductDetails();
        firstProductDetails.setTitle("Product1");
        firstProductDetails.setCount(2);
        firstProductDetails.setPrice(20.0);

        ProductDetails secondProductDetails = new ProductDetails();
        secondProductDetails.setTitle("Product2");
        secondProductDetails.setCount(1);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setProductDetails(List.of(firstProductDetails, secondProductDetails));

        kafkaMessageSender.send(orderDetails);

        Assertions.assertEquals(List.of(orderDetails), kafkaTestMessageSender.getSentMessages("payment"));
    }
}
