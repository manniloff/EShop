package com.amdaris.mentoring.payment.service.impl;

import com.amdaris.mentoring.payment.dto.converter.PaymentConverter;
import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;
import com.amdaris.mentoring.payment.util.exception.EntityExistsException;
import com.amdaris.mentoring.payment.model.PaymentHistory;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.model.PaymentStatus;
import com.amdaris.mentoring.payment.repository.PaymentRepository;
import com.amdaris.mentoring.payment.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
    private final PaymentRepository paymentRepository;

    @Override
    public List<PaymentHistoryDto> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentConverter.toPaymentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentHistoryDto> findById(long id) {
        return Optional.ofNullable(PaymentConverter.toPaymentDto
                .apply(paymentRepository.findById(id)
                        .get())
        );
    }

    @Override
    public Long save(PaymentHistoryDto paymentHistoryDto) {
        boolean isPresent = paymentRepository.existsByOrderId(paymentHistoryDto.getOrderId());
        if (isPresent) {
            throw new EntityExistsException("Payment for order - " + paymentHistoryDto.getOrderId() + " exists!");
        }
        paymentHistoryDto.setPaymentDate(LocalDateTime.now().withNano(0));
        return paymentRepository.save(PaymentConverter.toPayment.apply(paymentHistoryDto)).getId();
    }

    @Override
    public Long update(PaymentHistoryDto paymentHistoryDto, long id) {
        Optional<PaymentHistory> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            payment.get().setPaymentMethod(PaymentMethod.builder().title(paymentHistoryDto.getPaymentMethod()).build());
            payment.get().setPaymentDate(LocalDateTime.now().withNano(0));
            payment.get().setStatus(PaymentStatus.valueOf(paymentHistoryDto.getStatus()));
            payment.get().setOrderId(paymentHistoryDto.getOrderId());

            paymentRepository.save(payment.get());
            return payment.get().getId();
        } else {
            throw new EntityExistsException("Payment with id - " + id + " doesn't exist!");
        }
    }

    @Override
    public Long deleteById(long id) {
        Optional<PaymentHistory> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            paymentRepository.delete(payment.get());
            return payment.get().getId();
        } else {
            throw new EntityExistsException("Payment with id - " + id + " doesn't exist!");
        }
    }
}
