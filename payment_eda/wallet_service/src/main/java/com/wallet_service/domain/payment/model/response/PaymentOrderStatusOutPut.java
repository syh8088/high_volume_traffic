package com.wallet_service.domain.payment.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.payment.enums.PaymentOrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentOrderStatusOutPut {

    private long no;
    private String orderId;
    private PaymentOrderStatus status;
    private BigDecimal amount;

    @QueryProjection
    public PaymentOrderStatusOutPut(long no, String orderId, PaymentOrderStatus status, BigDecimal amount) {
        this.no = no;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
    }


}
