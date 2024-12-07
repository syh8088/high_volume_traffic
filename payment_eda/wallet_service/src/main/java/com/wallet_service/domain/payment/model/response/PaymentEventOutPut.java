package com.wallet_service.domain.payment.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.payment.enums.PaymentEventMethod;
import com.wallet_service.domain.payment.enums.PaymentEventType;
import com.wallet_service.domain.wallet.model.response.PaymentOrderOutPut;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentEventOutPut {

    private long paymentEventNo;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private PaymentEventMethod method;
    private PaymentEventType type;
    private LocalDateTime approvedDateTime;
    private boolean isPaymentDone;

    private List<PaymentOrderOutPut> paymentOrderList;

    @QueryProjection
    public PaymentEventOutPut(long paymentEventNo, String paymentKey, String orderId, String orderName, PaymentEventMethod method, PaymentEventType type, LocalDateTime approvedDateTime, boolean isPaymentDone) {
        this.paymentEventNo = paymentEventNo;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.method = method;
        this.type = type;
        this.approvedDateTime = approvedDateTime;
        this.isPaymentDone = isPaymentDone;
    }

    public static List<String> extractOrderIdList(List<PaymentEventOutPut> paymentEventList) {
        return paymentEventList.stream()
                .map(PaymentEventOutPut::getOrderId)
                .toList();
    }

    public void updatePaymentOrder(List<PaymentOrderOutPut> paymentOrderOutPuts) {
        this.paymentOrderList = paymentOrderOutPuts;
    }

    public BigDecimal getTotalAmount() {
        return this.paymentOrderList
                .stream()
                .map(PaymentOrderOutPut::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
