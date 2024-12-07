package com.payment_service.domain.payment.service;

import com.payment_service.domain.payment.model.entity.PaymentEvent;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentCheckOutCommendService {

    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;

    public void insertPaymentCheckOut(PaymentEvent paymentEvent) {
        paymentEventRepository.save(paymentEvent);
        paymentOrderRepository.saveAll(paymentEvent.getPaymentOrderList());
    }
}
