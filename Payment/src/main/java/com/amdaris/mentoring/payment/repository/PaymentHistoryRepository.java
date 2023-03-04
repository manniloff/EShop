package com.amdaris.mentoring.payment.repository;

import com.amdaris.mentoring.payment.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    boolean existsByOrderId(long orderId);

    PaymentHistory findByOrderId(long orderId);
}
