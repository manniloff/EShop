package com.amdaris.mentoring.payment.method.paypal.controller;

import com.amdaris.mentoring.payment.PaymentMicroservice;
import com.amdaris.mentoring.payment.method.OrderPaymentService;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.paypal.api.payments.Payment;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PaymentMicroservice.class)
@AutoConfigureMockMvc
public class PayPalControllerTestsIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderPaymentService<Payment, PaypalPaymentOrder> orderPaymentService;

    @Value("${test.paypal.payid}")
    private String payId;

    @Value("${test.paypal.payerId}")
    private String payerEmail;

  @DisplayName("Test that endpoint return if payment was executed successfully")
    @Test
    public void successPay_dataIsPresent_returnSuccessResponseMessage() throws Exception {
        Payment payment = new Payment();
        payment.setState("approved");

        Mockito.when(orderPaymentService.executePayment(payId, payerEmail)).thenReturn(payment);

        mvc.perform(MockMvcRequestBuilders.get("/payment/paypal/pay/success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("paymentId", payId)
                        .param("payerId", payerEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("Payment success", result.getResponse().getContentAsString()));
    }


    @DisplayName("Test that endpoint return that payment was pending")
    @Test
    public void successPay_dataIsPresent_returnPendingResponseMessage() throws Exception {
        Payment payment = new Payment();
        payment.setState("pending");

        Mockito.when(orderPaymentService.executePayment(payId, payerEmail)).thenReturn(payment);

        mvc.perform(MockMvcRequestBuilders.get("/payment/paypal/pay/success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("paymentId", payId)
                        .param("payerId", payerEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("Payment pending", result.getResponse().getContentAsString()));
    }

    @DisplayName("Test that endpoint return canceled payment message")
    @Test
    public void cancelPay_dataIsPresent_returnFailedResponseMessage() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/payment/paypal/pay/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(result -> assertEquals("Payment failed", result.getResponse().getContentAsString()));
    }
}
