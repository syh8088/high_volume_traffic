package com.payment_service.api.payment.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentEventWithOrderResponse {

    private List<PaymentEventResponse> paymentEventList;

    @Builder
    private PaymentEventWithOrderResponse(List<PaymentEventResponse> paymentEventList) {
        this.paymentEventList = paymentEventList;
    }

    public static PaymentEventWithOrderResponse of(List<PaymentEventResponse> paymentEventList) {
        return PaymentEventWithOrderResponse.builder()
                .paymentEventList(paymentEventList)
                .build();
    }
}
