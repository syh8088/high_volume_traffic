package com.payment_service.api.payment.service;

import com.payment_service.api.payment.model.response.PaymentEventOutPut;
import com.payment_service.domain.payment.service.PaymentEventQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApiService {

    private final PaymentEventQueryService paymentEventQueryService;

    @Transactional(readOnly = true)
    public List<PaymentEventOutPut> selectPayments() {
        return paymentEventQueryService.selectPayments();
    }
}
