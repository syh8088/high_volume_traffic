package com.payment_service.domain.payment.service;

import com.payment_service.api.payment.model.response.PaymentEventOutPut;
import com.payment_service.api.payment.model.response.PaymentOrderOutPut;
import com.payment_service.domain.payment.repository.PaymentEventRepository;
import com.payment_service.domain.payment.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentEventQueryService {

    private final PaymentEventCommendService paymentEventCommendService;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;

    @Transactional
    public void updatePaymentEventExtraDetails(
            String orderId,
            String paymentKey,
            String pspRawData,
            LocalDateTime approvedDateTime,
            boolean isPaymentDone
    ) {
        paymentEventCommendService.updatePaymentEventExtraDetails(
                orderId,
                paymentKey,
                pspRawData,
                approvedDateTime,
                isPaymentDone
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentEventOutPut> selectPayments() {

        List<PaymentEventOutPut> paymentEventList = paymentEventRepository.selectPaymentEventList();
        if (Objects.isNull(paymentEventList)) {
            return List.of();
        }

        List<String> orderIdList = PaymentEventOutPut.extractOrderIdList(paymentEventList);
        List<PaymentOrderOutPut> paymentOrderList = paymentOrderRepository.selectPaymentOrderListWithProductByOrderIdList(orderIdList);

        Map<String, List<PaymentOrderOutPut>> paymentOrderGroupingOrderIdMap = this.createPaymentOrderGroupingByOrderId(paymentOrderList);
        this.updatePaymentOrder(paymentEventList, paymentOrderGroupingOrderIdMap);

        return paymentEventList;
    }

    private Map<String, List<PaymentOrderOutPut>> createPaymentOrderGroupingByOrderId(List<PaymentOrderOutPut> paymentOrderList) {
        return paymentOrderList.stream()
                    .collect(Collectors.groupingBy(PaymentOrderOutPut::getOrderId));
    }

    private void updatePaymentOrder(List<PaymentEventOutPut> paymentEventList, Map<String, List<PaymentOrderOutPut>> paymentOrderGroupingOrderIdMap) {
        for (PaymentEventOutPut paymentEvent : paymentEventList) {
            List<PaymentOrderOutPut> paymentOrderOutPuts = paymentOrderGroupingOrderIdMap.get(paymentEvent.getOrderId());
            paymentEvent.updatePaymentOrder(paymentOrderOutPuts);
        }
    }
}
