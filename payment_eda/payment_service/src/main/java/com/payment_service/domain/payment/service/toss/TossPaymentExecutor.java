package com.payment_service.domain.payment.service.toss;

import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import com.payment_service.domain.payment.model.response.PaymentExecutionResultOutPut;
import com.payment_service.domain.payment.model.response.toss.TossPaymentConfirmationWithPspRawDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@RequiredArgsConstructor
public class TossPaymentExecutor {

    private final TossPaymentService tossPaymentService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PaymentExecutionResultOutPut paymentConfirm(PaymentConfirmInPut request) {

        TossPaymentConfirmationWithPspRawDataResponse response = tossPaymentService.paymentExecutor(request);
        return PaymentExecutionResultOutPut.of(request, response);
    }
}
