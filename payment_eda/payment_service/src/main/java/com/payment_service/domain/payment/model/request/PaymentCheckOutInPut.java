package com.payment_service.domain.payment.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentCheckOutInPut {

    private List<Long> productNoList = Arrays.asList(1L, 2L, 3L);

    @Builder
    private PaymentCheckOutInPut(List<Long> productNoList) {
        this.productNoList = productNoList;
    }

    public static PaymentCheckOutInPut of(List<Long> productNoLis) {
        return PaymentCheckOutInPut.builder()
                .productNoList(productNoLis)
                .build();
    }
}
