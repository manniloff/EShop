package com.amdaris.mentoring.payment.service.impl;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.dto.converter.PaymentMethodConverter;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import com.amdaris.mentoring.payment.service.PaymentMethodService;
import com.amdaris.mentoring.payment.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
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
    public PaymentMethodDto findById(short id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            return PaymentMethodConverter.toPaymentMethodDto
                    .apply(paymentMethod.get());
        }
        throw new NoSuchElementException("Payment method with id - " + id + " doesn't exist!");
    }

    @Override
    public PaymentMethod save(PaymentMethodDto paymentMethodDto) {
        boolean isPresent = paymentMethodRepository.existsByTitle(paymentMethodDto.getTitle());
        if (isPresent) {
            throw new EntityExistsException("Payment method with title - " + paymentMethodDto.getTitle() + " exists!");
        }
        return paymentMethodRepository.save(PaymentMethodConverter.toPaymentMethod.apply(paymentMethodDto));
    }

    @Override
    public PaymentMethod update(PaymentMethodDto paymentMethodDto, short id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        if (paymentMethod.isPresent()) {
            paymentMethod.get().setTitle(paymentMethodDto.getTitle());
            paymentMethod.get().setDetails(paymentMethodDto.getDetails());

            paymentMethodRepository.save(paymentMethod.get());
            return paymentMethod.get();
        }
        throw new NoSuchElementException("Payment method with id - " + id + " doesn't exist!");
    }

    @Override
    public short deleteById(short id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            paymentMethodRepository.deleteById(paymentMethod.get().getId());
            return paymentMethod.get().getId();
        } else {
            throw new EntityExistsException("Payment method with id - " + id + " doesn't exist!");
        }
    }

    @Override
    public PaymentMethod findByTitle(String title) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByTitle(title);
        if (Optional.ofNullable(paymentMethod).isPresent()) {
            return paymentMethod;
        } else {
            throw new NoSuchElementException("Payment method with title - " + title + " doesn't exist!");
        }
    }

    @Override
    public boolean existsByTitle(String title) {
        return paymentMethodRepository.existsByTitle(title);
    }

    @Override
    public void clear() {
        paymentMethodRepository.deleteAll();
    }
}
