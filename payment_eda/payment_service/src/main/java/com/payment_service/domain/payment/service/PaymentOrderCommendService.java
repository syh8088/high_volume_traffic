package com.payment_service.domain.payment.service;

import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import com.payment_service.domain.payment.model.entity.PaymentOrderHistory;
import com.payment_service.domain.payment.model.response.PaymentOrderStatusOutPut;
import com.payment_service.domain.payment.repository.PaymentOrderHistoryRepository;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentOrderCommendService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;


    public void insertPaymentOrderHistoryList(
            List<PaymentOrderStatusOutPut> paymentOrderStatusList,
            PaymentOrderStatus newPaymentStatus,
            String reason
    ) {
        List<PaymentOrderHistory> paymentOrderHistories = PaymentOrderHistory.of(paymentOrderStatusList, newPaymentStatus, reason);
        paymentOrderHistoryRepository.saveAll(paymentOrderHistories);
    }

    public void updatePaymentOrderStatusByOrderId(String orderId, PaymentOrderStatus paymentStatus) {
        paymentOrderRepository.updatePaymentOrderStatusByOrderId(orderId, paymentStatus);
    }
}
