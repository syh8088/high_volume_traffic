package com.payment_service.domain.payment.service;

import com.payment_service.domain.payment.model.response.PaymentExecutionResultOutPut;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentEventCommendService {

    private final PaymentEventRepository paymentEventRepository;

    public void updatePaymentEventExtraDetails(
            String orderId,
            String paymentKey,
            String pspRawData,
            LocalDateTime approvedDateTime,
            boolean isPaymentDone
    ) {
        paymentEventRepository.updatePaymentEventExtraDetails(
                orderId,
                paymentKey,
                pspRawData,
                approvedDateTime,
                isPaymentDone
        );
    }

    public void updateIsWalletDoneByOrderId(String orderId, boolean isWalletDone) {
        paymentEventRepository.updateIsWalletDoneByOrderId(orderId, isWalletDone);
    }
}
