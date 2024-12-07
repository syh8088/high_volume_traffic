package com.payment_service.domain.payment.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentConfirmInPut {

    private String paymentKey;
    private String orderId;
    private String amount;

    @Builder
    private PaymentConfirmInPut(String paymentKey, String orderId, String amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public static PaymentConfirmInPut of(String paymentKey, String orderId, String amount) {
        return new PaymentConfirmInPut(paymentKey, orderId, amount);
    }
}
