package com.payment_service.domain.payment.service;

import com.payment_service.domain.payment.enums.PaymentEventMethod;
import com.payment_service.domain.payment.enums.PaymentEventType;
import com.payment_service.domain.payment.model.entity.PaymentEvent;
import com.payment_service.domain.payment.model.response.PaymentCheckOutOutPut;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentCheckOutQueryService {

    private final PaymentCheckOutCommendService paymentCheckOutCommendService;
    private final PaymentEventRepository paymentEventRepository;

    @Transactional
    public PaymentCheckOutOutPut paymentCheckOut(String idempotency, List<ProductOutPut> productList) {

        PaymentEvent paymentEvent = PaymentEvent.of(
                idempotency,
                PaymentEventMethod.CARD,
                PaymentEventType.NORMAL,
                productList
        );

        paymentCheckOutCommendService.insertPaymentCheckOut(paymentEvent);

        return PaymentCheckOutOutPut.of(
                paymentEvent.getTotalAmount(),
                idempotency,
                paymentEvent.getOrderName()
        );
    }
}
