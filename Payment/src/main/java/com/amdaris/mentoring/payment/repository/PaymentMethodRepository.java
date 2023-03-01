package com.amdaris.mentoring.payment.repository;

import com.amdaris.mentoring.payment.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Short> {
    boolean existsByTitle(String title);

    PaymentMethod findByTitle(String title);
}
