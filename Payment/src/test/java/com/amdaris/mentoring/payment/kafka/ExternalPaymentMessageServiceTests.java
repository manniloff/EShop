package com.amdaris.mentoring.payment.kafka;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.payment.env.ExternalMessageTestConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Import(ExternalMessageTestConfiguration.class)
@SpringBootTest
class ExternalPaymentMessageServiceTests {
    @Autowired
    private ExternalMessageTestConfiguration externalMessageTestConfiguration;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${externalmessage.topics.payment}")
    private String topic;

    @BeforeEach
    public void beforeEach() {
        externalMessageTestConfiguration.reset();
    }

    @DisplayName("Test that invalid messages silently ignored")
    @Test
    public void sendKafkaMessage_dataIsInvalid_ignoreWrongMessages() throws JsonProcessingException {
        List<String> invalidPaymentMessages = new ArrayList<>();

        invalidPaymentMessages.add(null);
        invalidPaymentMessages.add("INCORRECT_JSON");

        invalidPaymentMessages.forEach(
                invalidPaymentMessage -> externalMessageTestConfiguration.externalMessageService()
                        .send(topic, invalidPaymentMessage));

        List<String> receivedMessages = externalMessageTestConfiguration.getSentMessages(topic);
        OrderDetails firstMessageReceived = objectMapper.readValue(receivedMessages.get(0), OrderDetails.class);
        String secondMessageReceived = receivedMessages.get(1);

        Assertions.assertNull(firstMessageReceived);
        Assertions.assertNotEquals(secondMessageReceived.getClass(), OrderDetails.class);
    }

    @DisplayName("Test that messages that were sent to kafka for payment topic were received")
    @Test
    public void sendKafkaMessage_dataIsValid_sentAllData() {
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

        Assertions.assertTrue(true);
    }
}