package com.amdaris.mentoring.payment.repository;

import com.amdaris.mentoring.payment.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentHistory, Long> {
    boolean existsByOrderId(Long orderId);
}
