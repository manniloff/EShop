package com.amdaris.mentoring.payment.kafka;

import com.amdaris.mentoring.payment.model.OrderDetails;
import com.amdaris.mentoring.payment.service.PaymentService;
import com.amdaris.mentoring.payment.util.json.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalPaymentMessageService implements ExternalMessageService.ReceiveListener {
    private final ExternalMessageService externalMessageService;
    private final JsonService jsonService;
    private final List<PaymentService> paymentService;

    @Value("${externalmessage.topics.payment}")
    private String topic;

    @PostConstruct
    public void init() {
        externalMessageService.addReceiveListener(topic, this);
    }



    @Override
    public void onReceive(String topic, String message) {
        try {
            OrderDetails orderDetails = jsonService.fromJson(message, OrderDetails.class);

            System.out.println(orderDetails);
            /*Optional<PaymentService> payService = paymentService.stream()
                    .filter(service -> service.getClass().getName().contains(paymentDetails.getMethod()))
                    .findFirst();

            if (payService.isPresent()) {
                payService.get().payOrder(paymentDetails);
            }*/
        } catch (Exception ex) {
            log.warn("Event handling error, processing by subsequent listeners skipped {}", ex.getMessage(), ex);
        }
    }
}
