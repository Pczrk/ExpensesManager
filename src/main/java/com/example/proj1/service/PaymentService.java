package com.example.proj1.service;

import com.example.proj1.repository.PaymentRepository;
import com.example.proj1.repository.entity.Payment;
import com.example.proj1.repository.entity.Trip;
import com.example.proj1.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createPaymentEntity(Trip trip, User user, BigDecimal value){
        paymentRepository.save(Payment.builder()
                        .trip(trip)
                        .payer(user)
                        .value(value)
                .build());
    }
}
