package com.amdaris.mentoring.payment.service.impl;

import com.amdaris.mentoring.payment.dto.converter.PaymentMethodConverter;
import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.exception.EntityExistsException;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.repository.PaymentMethodRepository;
import com.amdaris.mentoring.payment.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodDto> findAll() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(PaymentMethodConverter.toPaymentMethodDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentMethodDto> findById(Short id) {
        return Optional.ofNullable(PaymentMethodConverter.toPaymentMethodDto
                .apply(paymentMethodRepository.findById(id)
                        .get())
        );
    }

    @Override
    public Short save(PaymentMethodDto paymentMethodDto) {
        boolean isPresent = paymentMethodRepository.existsByTitle(paymentMethodDto.getTitle());
        if (isPresent) {
            throw new EntityExistsException("Payment method with title - " + paymentMethodDto.getTitle() + " exists!");
        }
        return paymentMethodRepository.save(PaymentMethodConverter.toPaymentMethod.apply(paymentMethodDto)).getId();
    }

    @Override
    public Short update(PaymentMethodDto paymentMethodDto, short id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        if (paymentMethod.isPresent()) {
            paymentMethod.get().setTitle(paymentMethodDto.getTitle());
            paymentMethod.get().setDetails(paymentMethodDto.getDetails());

            paymentMethodRepository.save(paymentMethod.get());
            return paymentMethod.get().getId();
        } else {
            throw new EntityExistsException("Payment method with id - " + id + " doesn't exist!");
        }
    }

    @Override
    public Short deleteById(short id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            paymentMethodRepository.deleteById(paymentMethod.get().getId());
            return paymentMethod.get().getId();
        } else {
            throw new EntityExistsException("Payment method with id - " + id + " doesn't exist!");
        }
    }
}
