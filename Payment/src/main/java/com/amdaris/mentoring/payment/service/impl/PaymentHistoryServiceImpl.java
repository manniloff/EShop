package com.amdaris.mentoring.payment.service.impl;

import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;
import com.amdaris.mentoring.payment.dto.converter.PaymentHistoryConverter;
import com.amdaris.mentoring.payment.model.PaymentHistory;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.model.PaymentStatus;
import com.amdaris.mentoring.payment.repository.PaymentHistoryRepository;
import com.amdaris.mentoring.payment.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Override
    public List<PaymentHistoryDto> findAll() {
        return paymentHistoryRepository.findAll()
                .stream()
                .map(PaymentHistoryConverter.toPaymentDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentHistoryDto findById(long id) {
        Optional<PaymentHistory> findById = paymentHistoryRepository.findById(id);
        if (findById.isPresent()) {
            return PaymentHistoryConverter.toPaymentDto
                    .apply(findById.get());
        }
        throw new NoSuchElementException("Payment history with id - " + id + " doesn't exist!");
    }

    @Override
    public Long save(PaymentHistoryDto paymentHistoryDto) {
        boolean isPresent = paymentHistoryRepository.existsByOrderId(paymentHistoryDto.getOrderId());
        if (isPresent) {
            throw new EntityExistsException("Payment history for order id - " + paymentHistoryDto.getOrderId() + " exists!");
        }
        paymentHistoryDto.setPaymentDate(LocalDateTime.now().withNano(0));
        return paymentHistoryRepository.save(PaymentHistoryConverter.toPayment.apply(paymentHistoryDto)).getId();
    }

    @Override
    public Long update(PaymentHistoryDto paymentHistoryDto, long id) {
        Optional<PaymentHistory> payment = paymentHistoryRepository.findById(id);

        if (payment.isPresent()) {
            payment.get().setPaymentMethod(PaymentMethod.builder().title(paymentHistoryDto.getPaymentMethod()).build());
            payment.get().setPaymentDate(LocalDateTime.now().withNano(0));
            payment.get().setStatus(PaymentStatus.valueOf(paymentHistoryDto.getStatus()));
            payment.get().setOrderId(paymentHistoryDto.getOrderId());

            paymentHistoryRepository.save(payment.get());
            return payment.get().getId();
        } else {
            throw new NoSuchElementException("Payment history with id - " + id + " doesn't exist!");
        }
    }

    @Override
    public Long deleteById(long id) {
        Optional<PaymentHistory> payment = paymentHistoryRepository.findById(id);
        if (payment.isPresent()) {
            paymentHistoryRepository.delete(payment.get());
            return payment.get().getId();
        } else {
            throw new EntityExistsException("Payment history with id - " + id + " doesn't exist!");
        }
    }

    @Override
    public PaymentHistory findByOrderId(long orderId) {
        PaymentHistory byOrderId = paymentHistoryRepository.findByOrderId(orderId);

        if (Optional.ofNullable(byOrderId).isPresent()) {
            return byOrderId;
        }
        throw new NoSuchElementException("Payment history with order id - " + orderId + " doesn't exist!");
    }

    @Override
    public void clear() {
        paymentHistoryRepository.deleteAll();
    }
}
