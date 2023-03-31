package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.core.CoreMicroservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class OrderControllerTestsIT {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that was got order detail message and sent to kafka")
    @Test
    public void testThatWasGotOrderDetailsMessageAndSentToKafka() throws Exception {
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
        orderDetails.setProductDetails(List.of(firstProductDetails, secondProductDetails));
        orderDetails.setShippingPrice(10.0);

        mvc.perform(MockMvcRequestBuilders.post("/order/send/to/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("message was sent to kafka",
                        result.getResponse().getContentAsString()));
    }
}
