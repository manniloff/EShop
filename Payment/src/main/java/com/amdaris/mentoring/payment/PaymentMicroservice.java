package com.amdaris.mentoring.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("com.amdaris.mentoring.payment")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class PaymentMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMicroservice.class);
    }
}
