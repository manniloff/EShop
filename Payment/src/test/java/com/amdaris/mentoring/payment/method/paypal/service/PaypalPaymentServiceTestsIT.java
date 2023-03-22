package com.amdaris.mentoring.payment.method.paypal.service;

import com.amdaris.mentoring.common.model.OrderDetails;
import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.payment.PaymentMicroservice;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.amdaris.mentoring.payment.model.PaymentDetails;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PaymentMicroservice.class)
@AutoConfigureMockMvc
public class PaypalPaymentServiceTestsIT {
    @Autowired
    private PaypalPaymentService paypalPaymentService;

    @Value("${paypal.payment.success}")
    private String success;

    @Value("${paypal.payment.cancel}")
    private String cancel;

    @Value("${paypal.payment.host}")
    private String host;

    @Value("${test.paypal.payid}")
    private String payId;

    @Value("${test.paypal.payerId}")
    private String payerEmail;

    @DisplayName("Test that oder was paid")
    @Test
    public void payOrder_dataIsPresent_returnSuccessResult() throws Exception {
        ProductDetails fristProductDetails = new ProductDetails();
        fristProductDetails.setTitle("First products");
        fristProductDetails.setPrice(15.0);
        fristProductDetails.setCount(1);

        ProductDetails secondProductDetails = new ProductDetails();
        secondProductDetails.setTitle("Second products");
        secondProductDetails.setPrice(10.0);
        secondProductDetails.setCount(2);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1L);
        orderDetails.setProductDetails(List.of(fristProductDetails, secondProductDetails));
        orderDetails.setShippingPrice(20);

        PaymentDetails orderPaymentDetails = new PaymentDetails();

        orderPaymentDetails.setDetails("Test Details");
        orderPaymentDetails.setOrderDetails(orderDetails);
        orderPaymentDetails.setCurrency("USD");
        orderPaymentDetails.setMethod("paypal");

        String response = paypalPaymentService.payOrder(orderPaymentDetails);

        Assertions.assertTrue(response.contains("www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="));
    }

    @DisplayName("Test that we create Payment and got result")
    @Test
    public void createPayment_dataIsPresent_returnPayment() throws Exception {
        ProductDetails fristProductDetails = new ProductDetails();
        fristProductDetails.setTitle("First products");
        fristProductDetails.setPrice(15.0);
        fristProductDetails.setCount(1);

        ProductDetails secondProductDetails = new ProductDetails();
        secondProductDetails.setTitle("Second products");
        secondProductDetails.setPrice(10.0);
        secondProductDetails.setCount(2);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1L);
        orderDetails.setProductDetails(List.of(fristProductDetails, secondProductDetails));
        orderDetails.setShippingPrice(20);

        PaymentDetails orderPaymentDetails = new PaymentDetails();

        orderPaymentDetails.setDetails("Test Details");
        orderPaymentDetails.setOrderDetails(orderDetails);
        orderPaymentDetails.setCurrency("USD");
        orderPaymentDetails.setMethod("paypal");

        double total = orderPaymentDetails.getOrderDetails()
                .getProductDetails()
                .stream()
                .map(ProductDetails::getPrice)
                .reduce(0.0, Double::sum) + orderPaymentDetails.getOrderDetails().getShippingPrice();

        PaypalPaymentOrder paypalPaymentOrder = new PaypalPaymentOrder();
        paypalPaymentOrder.setTotal(total);
        paypalPaymentOrder.setCurrency(orderPaymentDetails.getCurrency());
        paypalPaymentOrder.setMethod(orderPaymentDetails.getMethod());
        paypalPaymentOrder.setIntent("sale");
        paypalPaymentOrder.setDescription(orderPaymentDetails.getDetails());
        paypalPaymentOrder.setSuccessUrl(host + success);
        paypalPaymentOrder.setCancelUrl(host + cancel);

        Payment payment = paypalPaymentService.createPayment(paypalPaymentOrder);

        Assertions.assertEquals(paypalPaymentOrder.getIntent(), payment.getIntent());
        Assertions.assertEquals(orderPaymentDetails.getDetails(), payment.getTransactions().get(0).getDescription());
        Assertions.assertEquals("created", payment.getState());
    }

    @DisplayName("Test that we create Payment and got result")
    @Test
    public void executePayment_dataIsPresent_returnPayment() throws PayPalRESTException {
        PayPalRESTException exception = Assertions.assertThrows(PayPalRESTException.class,
                () -> paypalPaymentService.executePayment(payId, payerEmail));

        Assertions.assertTrue(exception.getMessage().contains("Error code : 404 with response : {\"name\":\"INVALID_RESOURCE_ID\",\"message\":\"Requested resource ID was not found.\""));
    }
}
