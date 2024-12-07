package com.payment_service.api.payment.model.response;

import com.payment_service.domain.payment.enums.PaymentEventMethod;
import com.payment_service.domain.payment.enums.PaymentEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentEventResponse {

    private long paymentEventNo;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private PaymentEventMethod method;
    private PaymentEventType type;
    private LocalDateTime approvedDateTime;
    private boolean isPaymentDone;
    private BigDecimal totalAmount;

    private List<PaymentOrderResponse> paymentOrderList;

    @Builder
    private PaymentEventResponse(long paymentEventNo, String paymentKey, String orderId, String orderName, PaymentEventMethod method, PaymentEventType type, LocalDateTime approvedDateTime, boolean isPaymentDone, BigDecimal totalAmount, List<PaymentOrderResponse> paymentOrderList) {
        this.paymentEventNo = paymentEventNo;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.method = method;
        this.type = type;
        this.approvedDateTime = approvedDateTime;
        this.isPaymentDone = isPaymentDone;
        this.totalAmount = totalAmount;
        this.paymentOrderList = paymentOrderList;
    }

    public static PaymentEventResponse of(PaymentEventOutPut paymentEventOutPut) {

        List<PaymentOrderOutPut> paymentOrderList = paymentEventOutPut.getPaymentOrderList();
        List<PaymentOrderResponse> paymentOrderResponses = PaymentOrderResponse.of(paymentOrderList);

        return PaymentEventResponse.builder()
                .paymentEventNo(paymentEventOutPut.getPaymentEventNo())
                .paymentKey(paymentEventOutPut.getPaymentKey())
                .orderId(paymentEventOutPut.getOrderId())
                .orderName(paymentEventOutPut.getOrderName())
                .method(paymentEventOutPut.getMethod())
                .type(paymentEventOutPut.getType())
                .approvedDateTime(paymentEventOutPut.getApprovedDateTime())
                .isPaymentDone(paymentEventOutPut.isPaymentDone())
                .totalAmount(getTotalAmount(paymentOrderResponses))
                .paymentOrderList(paymentOrderResponses)
                .build();
    }

    public static List<PaymentEventResponse> of(List<PaymentEventOutPut> paymentEventOutPutList) {


        return paymentEventOutPutList.stream()
                .map(PaymentEventResponse::of)
                .toList();
    }

    private static BigDecimal getTotalAmount(List<PaymentOrderResponse> paymentOrderResponses) {
        return paymentOrderResponses
                .stream()
                .map(PaymentOrderResponse::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
