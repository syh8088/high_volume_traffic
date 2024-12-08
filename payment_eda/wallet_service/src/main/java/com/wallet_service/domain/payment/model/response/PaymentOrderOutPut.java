package com.wallet_service.domain.payment.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.payment.enums.PaymentOrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentOrderOutPut {

    private long paymentOrderNo;
    private String orderId;
    private BigDecimal amount;
    private PaymentOrderStatus status;

    private long productNo;
    private String productId;
    private String name;
    private BigDecimal price;

    @QueryProjection
    public PaymentOrderOutPut(long paymentOrderNo, String orderId, BigDecimal amount, PaymentOrderStatus status, long productNo, String productId, String name, BigDecimal price) {
        this.paymentOrderNo = paymentOrderNo;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.productNo = productNo;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }
}
