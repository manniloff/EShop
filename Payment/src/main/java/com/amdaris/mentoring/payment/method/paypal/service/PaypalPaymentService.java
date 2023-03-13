package com.amdaris.mentoring.payment.method.paypal.service;

import com.amdaris.mentoring.common.model.ProductDetails;
import com.amdaris.mentoring.payment.method.OrderPaymentService;
import com.amdaris.mentoring.payment.method.paypal.model.PaypalPaymentOrder;
import com.amdaris.mentoring.payment.model.PaymentDetails;
import com.amdaris.mentoring.payment.service.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalPaymentService implements OrderPaymentService<Payment, PaypalPaymentOrder>, PaymentService {
    @Value("${paypal.payment.success}")
    private String success;

    @Value("${paypal.payment.cancel}")
    private String cancel;

    @Value("${paypal.payment.host}")
    private String host;
    private final APIContext apiContext;

    @Override
    public String payOrder(PaymentDetails orderPaymentDetails) throws Exception {
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

        Payment paymentData = createPayment(paypalPaymentOrder);

        for (Links link : paymentData.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return "redirect:" + link.getHref();
            }
        }

        return "Nothing happened";
    }

    @Override
    public Payment createPayment(PaypalPaymentOrder order) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(order.getCurrency());
        double total = new BigDecimal(order.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(order.getDescription());
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(order.getMethod());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(order.getCancelUrl());
        redirectUrls.setReturnUrl(order.getSuccessUrl());

        Payment payment = new Payment();
        payment.setIntent(order.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}
