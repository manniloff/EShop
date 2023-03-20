package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.PaymentMicroservice;
import com.amdaris.mentoring.payment.dto.converter.PaymentHistoryConverter;
import com.amdaris.mentoring.payment.model.PaymentHistory;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.model.PaymentStatus;
import com.amdaris.mentoring.payment.repository.PaymentHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
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

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PaymentMicroservice.class)
@AutoConfigureMockMvc
public class PaymentHistoryControllerTests {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("Test that endpoint return all payment methods")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        paymentHistoryRepository.deleteAll();

        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("cash");
        cardPaymentMethod.setDetails("pay with cash");

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setOrderId(1L);
        paymentHistory.setStatus(PaymentStatus.valueOf("complete"));
        paymentHistory.setPaymentMethod(cardPaymentMethod);
        paymentHistory.setPaymentDate(LocalDateTime.now().withNano(0));

        paymentHistoryRepository.save(paymentHistory);

        String expected = objectMapper.writeValueAsString(List.of(PaymentHistoryConverter.toPaymentDto.apply(paymentHistory)));

        mvc.perform(MockMvcRequestBuilders.get("/payment/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return payment history by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        paymentHistoryRepository.deleteAll();

        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setOrderId(1L);
        paymentHistory.setStatus(PaymentStatus.valueOf("complete"));
        paymentHistory.setPaymentMethod(cardPaymentMethod);
        paymentHistory.setPaymentDate(LocalDateTime.now().withNano(0));

        paymentHistoryRepository.save(paymentHistory);

        String expected = objectMapper.writeValueAsString(PaymentHistoryConverter.toPaymentDto.apply(paymentHistory));

        long paymentHistoryId = paymentHistoryRepository.findAll()
                .stream()
                .filter(paymentMethod -> paymentMethod.getOrderId().equals(paymentHistory.getOrderId()))
                .findFirst()
                .get()
                .getId();

        mvc.perform(MockMvcRequestBuilders.get("/payment/history/" + paymentHistoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists payment history by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        paymentHistoryRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/payment/history/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Payment history with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted payment history by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        paymentHistoryRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setOrderId(1L);
        paymentHistory.setStatus(PaymentStatus.valueOf("complete"));
        paymentHistory.setPaymentMethod(cardPaymentMethod);
        paymentHistory.setPaymentDate(LocalDateTime.now().withNano(0));

        paymentHistoryRepository.save(paymentHistory);

        long paymentHistoryId = paymentHistoryRepository.findAll()
                .stream()
                .filter(payment -> payment.getOrderId().equals(paymentHistory.getOrderId()))
                .findFirst()
                .get()
                .getId();

        cardPaymentMethod.setDetails("updated card details");
        mvc.perform(MockMvcRequestBuilders.delete("/payment/history/" + paymentHistoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Payment history with id - " + paymentHistoryId + ", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete payment history which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        paymentHistoryRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/payment/history/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Payment history with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }
}
