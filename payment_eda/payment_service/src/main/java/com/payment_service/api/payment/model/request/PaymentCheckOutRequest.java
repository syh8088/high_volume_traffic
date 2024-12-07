package com.payment_service.api.payment.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentCheckOutRequest {

    private List<Long> productNoList = Arrays.asList(1L, 2L, 3L);

    @Builder
    private PaymentCheckOutRequest(List<Long> productNoList) {
        this.productNoList = productNoList;
    }

    public static PaymentCheckOutRequest of(List<Long> productNoList) {
        return PaymentCheckOutRequest.builder()
                .productNoList(productNoList)
                .build();
    }
}