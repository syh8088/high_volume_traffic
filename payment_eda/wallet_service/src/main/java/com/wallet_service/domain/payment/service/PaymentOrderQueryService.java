package com.wallet_service.domain.payment.service;

import com.wallet_service.domain.payment.model.response.PaymentOrderWithSellerOutPut;
import com.wallet_service.domain.payment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentOrderQueryService {

    private final PaymentOrderRepository paymentOrderRepository;


    public List<PaymentOrderWithSellerOutPut> selectPaymentOrderListWithSellerByOrderId(String orderId) {
        return paymentOrderRepository.selectPaymentOrderListWithSellerByOrderId(orderId);
    }
}
