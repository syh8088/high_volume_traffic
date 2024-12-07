package com.payment_service.domain.payment.service;

import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import com.payment_service.domain.payment.model.response.PaymentOrderStatusOutPut;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentOrderQueryService {

    private final PaymentOrderCommendService paymentOrderCommendService;
    private final PaymentOrderRepository paymentOrderRepository;


    public List<PaymentOrderStatusOutPut> selectPaymentOrderStatusListByOrderId(String orderId) {
        return paymentOrderRepository.selectPaymentOrderStatusListByOrderId(orderId);
    }

    @Transactional
    public void insertPaymentOrderHistoryList(
            List<PaymentOrderStatusOutPut> paymentOrderStatusList,
            PaymentOrderStatus newPaymentStatus,
            String reason
    ) {

        paymentOrderCommendService.insertPaymentOrderHistoryList(
                paymentOrderStatusList,
                newPaymentStatus,
                reason
        );
    }

    @Transactional
    public void updatePaymentOrderStatusByOrderId(String orderId, PaymentOrderStatus paymentStatus) {
        paymentOrderCommendService.updatePaymentOrderStatusByOrderId(orderId, paymentStatus);
    }

    public BigDecimal selectTotalAmountByOrderId(String orderId) {
        return paymentOrderRepository.selectTotalAmountByOrderId(orderId);
    }
}
