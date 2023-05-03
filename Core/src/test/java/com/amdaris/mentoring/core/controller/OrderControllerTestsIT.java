package com.amdaris.mentoring.core.controller;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.core.CoreMicroservice;
import com.amdaris.mentoring.core.dto.OrderDto;
import com.amdaris.mentoring.core.dto.converter.OrderConverter;
import com.amdaris.mentoring.core.model.Order;
import com.amdaris.mentoring.core.repository.OrderRepository;
import com.amdaris.mentoring.core.util.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CoreMicroservice.class)
@AutoConfigureMockMvc
public class OrderControllerTestsIT {
    @Autowired
    private OrderRepository orderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("Test that endpoint return all orders")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now());
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        Order secondOrder = new Order();
        secondOrder.setTransId(transId);
        secondOrder.setStatus(OrderStatus.paid);
        secondOrder.setCreationDate(LocalDateTime.now().minusDays(1));
        secondOrder.setProducts(List.of());
        secondOrder.setOrderPrice(1500.0);

        orderRepository.saveAll(List.of(firstOrder, secondOrder));

        List<OrderDto> orders = orderRepository.findAll().stream()
                .map(OrderConverter.toOrderDto)
                .collect(Collectors.toList());

        mvc.perform(MockMvcRequestBuilders.get("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(orders),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return order by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        OrderDto orderDto = OrderConverter.toOrderDto.apply(orderRepository.save(firstOrder));

        mvc.perform(MockMvcRequestBuilders.get("/order/" + orderDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(orderDto),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists order by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        orderRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/order/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Order with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new order")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        OrderDto orderDto = OrderConverter.toOrderDto.apply(firstOrder);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        OrderDto response = OrderConverter.toOrderDto.apply(orderRepository.findAll().get(0));

        Assertions.assertEquals(objectMapper.writeValueAsString(response), mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Test that exception throws when try to create order which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        orderRepository.save(firstOrder);

        mvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrder)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Order with transId - " + transId + " already exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that order was updated")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        OrderDto order = OrderConverter.toOrderDto.apply(orderRepository.save(firstOrder));

        order.setStatus("paid");
        mvc.perform(MockMvcRequestBuilders.patch("/order/" + order.getTransId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(objectMapper.writeValueAsString(order),
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to update order which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        OrderDto order = OrderConverter.toOrderDto.apply(firstOrder);

        mvc.perform(MockMvcRequestBuilders.patch("/order/" + transId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Order with transId - " + transId + " doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted product by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        orderRepository.deleteAll();
        UUID transId = UUID.randomUUID();

        Order firstOrder = new Order();
        firstOrder.setTransId(transId);
        firstOrder.setStatus(OrderStatus.created);
        firstOrder.setCreationDate(LocalDateTime.now().withNano(0));
        firstOrder.setProducts(List.of());
        firstOrder.setOrderPrice(1000.0);

        Order order = orderRepository.save(firstOrder);

        mvc.perform(MockMvcRequestBuilders.delete("/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Order with id - " + order.getId() + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete product which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        orderRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/order/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Order with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

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

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setProductDetails(List.of(firstProductDetails, secondProductDetails));

        mvc.perform(MockMvcRequestBuilders.post("/order/send/to/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("message was sent to kafka",
                        result.getResponse().getContentAsString()));
    }
}
