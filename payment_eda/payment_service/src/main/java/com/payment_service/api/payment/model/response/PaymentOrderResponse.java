package com.payment_service.api.payment.model.response;

import com.payment_service.domain.payment.enums.PaymentOrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentOrderResponse {

    private long paymentOrderNo;
    private String orderId;
    private BigDecimal amount;
    private PaymentOrderStatus status;

    private long productNo;
    private String productId;
    private String name;
    private BigDecimal price;

    @Builder
    private PaymentOrderResponse(long paymentOrderNo, String orderId, BigDecimal amount, PaymentOrderStatus status, long productNo, String productId, String name, BigDecimal price) {
        this.paymentOrderNo = paymentOrderNo;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.productNo = productNo;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public static PaymentOrderResponse of(PaymentOrderOutPut paymentOrderOutPut) {
        return PaymentOrderResponse.builder()
                .paymentOrderNo(paymentOrderOutPut.getPaymentOrderNo())
                .orderId(paymentOrderOutPut.getOrderId())
                .amount(paymentOrderOutPut.getAmount())
                .status(paymentOrderOutPut.getStatus())
                .productNo(paymentOrderOutPut.getProductNo())
                .productId(paymentOrderOutPut.getProductId())
                .name(paymentOrderOutPut.getName())
                .price(paymentOrderOutPut.getPrice())
                .build();
    }

    public static List<PaymentOrderResponse> of(List<PaymentOrderOutPut> paymentOrderOutPutList) {
        return paymentOrderOutPutList.stream()
                .map(PaymentOrderResponse::of)
                .toList();
    }
}
