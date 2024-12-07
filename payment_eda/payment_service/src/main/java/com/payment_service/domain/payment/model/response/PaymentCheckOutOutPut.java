package com.payment_service.domain.payment.model.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentCheckOutOutPut {

    private final BigDecimal amount;
    private final String orderId;
    private final String orderName;

    @Builder
    private PaymentCheckOutOutPut(BigDecimal amount, String orderId, String orderName) {
        this.amount = amount;
        this.orderId = orderId;
        this.orderName = orderName;
    }

    public static PaymentCheckOutOutPut of(BigDecimal amount, String orderId, String orderName) {
        return new PaymentCheckOutOutPut(amount, orderId, orderName);
    }
}