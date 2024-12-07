package com.payment_service.api.payment.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentConfirmRequest {

    private String paymentKey;
    private String orderId;
    private String amount;

    @Builder
    private PaymentConfirmRequest(String paymentKey, String orderId, String amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public static PaymentConfirmRequest of(String paymentKey, String orderId, String amount) {
        return new PaymentConfirmRequest(paymentKey, orderId, amount);
    }
}
