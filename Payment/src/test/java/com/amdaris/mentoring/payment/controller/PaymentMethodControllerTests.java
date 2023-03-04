package com.amdaris.mentoring.payment.controller;

import com.amdaris.mentoring.payment.PaymentMicroservice;
import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.dto.converter.PaymentMethodConverter;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.repository.PaymentMethodRepository;
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

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PaymentMicroservice.class)
@AutoConfigureMockMvc
public class PaymentMethodControllerTests {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    @DisplayName("Test that endpoint return all payment methods")
    @Test
    public void findAll_dataIsPresent_returnAllData() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodRepository.save(cardPaymentMethod);

        List<PaymentMethod> findAll = paymentMethodRepository.findAll();

        String expectedList = objectMapper.writeValueAsString(findAll.stream()
                .map(method -> PaymentMethodConverter.toPaymentMethodDto.apply(method))
                .collect(Collectors.toList()));

        mvc.perform(MockMvcRequestBuilders.get("/payment/method")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(expectedList, result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return payment method by id")
    @Test
    public void findById_dataIsPresent_returnExistingData() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("cash");
        cardPaymentMethod.setDetails("pay with cash");

        paymentMethodRepository.save(cardPaymentMethod);

        String expected = objectMapper.writeValueAsString(PaymentMethodConverter.toPaymentMethodDto.apply(cardPaymentMethod));

        short paymentMethodId = paymentMethodRepository.findAll().stream().filter(paymentMethod -> paymentMethod.getTitle().equals("cash")).findFirst().get().getId();

        mvc.perform(MockMvcRequestBuilders.get("/payment/method/" + paymentMethodId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(expected, result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint throw error message when try to get not exists payment method by id")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() throws Exception {
        paymentMethodRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.get("/payment/method/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Payment method with id - 1 doesn't exist!", result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that endpoint create new payment method")
    @Test
    public void create_dataNoPresent_returnSavedDataId() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("paypal");
        cardPaymentMethod.setDetails("pay with paypal service");

        mvc.perform(MockMvcRequestBuilders.post("/payment/method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardPaymentMethod)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Created", result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to create payment method which already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodRepository.save(cardPaymentMethod);

        mvc.perform(MockMvcRequestBuilders.post("/payment/method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardPaymentMethod)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Payment method with title - card exists!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was updated payment method")
    @Test
    public void update_dataIsPresent_returnUpdatedDataId() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodRepository.save(cardPaymentMethod);

        Optional<PaymentMethod> card = paymentMethodRepository.findAll()
                .stream()
                .filter(paymentMethod -> paymentMethod.getTitle().equals("card"))
                .findFirst();

        if (card.isPresent()) {
            cardPaymentMethod.setDetails("updated card details");
            mvc.perform(MockMvcRequestBuilders.put("/payment/method/" + card.get().getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cardPaymentMethod)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(result -> assertEquals("Updated", result.getResponse().getContentAsString()));
        } else {
            paymentMethodRepository.save(cardPaymentMethod);

            mvc.perform(MockMvcRequestBuilders.put("/payment/method/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cardPaymentMethod)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(result -> assertEquals("Updated", result.getResponse().getContentAsString()));
        }
    }

    @DisplayName("Test that exception throws when try to update payment method which not exist")
    @Test
    public void update_dataNoPresent_returnErrorMessage() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        mvc.perform(MockMvcRequestBuilders.put("/payment/method/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardPaymentMethod)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andExpect(result -> assertEquals("Payment method with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }

    @DisplayName("Test that was deleted payment method by id")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() throws Exception {
        paymentMethodRepository.deleteAll();
        PaymentMethod cardPaymentMethod = new PaymentMethod();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodRepository.save(cardPaymentMethod);

        short paymentMethodId = paymentMethodRepository.findAll()
                .stream()
                .filter(paymentMethod -> paymentMethod.getTitle().equals("card"))
                .findFirst()
                .get()
                .getId();

        cardPaymentMethod.setDetails("updated card details");
        mvc.perform(MockMvcRequestBuilders.delete("/payment/method/" + paymentMethodId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals("Payment method with id - " + paymentMethodId +", was deleted",
                        result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that exception throws when try to delete payment method which not exist")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() throws Exception {
        paymentMethodRepository.deleteAll();

        mvc.perform(MockMvcRequestBuilders.delete("/payment/method/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityExistsException))
                .andExpect(result -> assertEquals("Payment method with id - 1 doesn't exist!",
                        result.getResolvedException().getMessage()));
    }
}
