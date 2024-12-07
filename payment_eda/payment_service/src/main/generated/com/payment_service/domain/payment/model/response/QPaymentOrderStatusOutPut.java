package com.payment_service.domain.payment.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.payment_service.domain.payment.model.response.QPaymentOrderStatusOutPut is a Querydsl Projection type for PaymentOrderStatusOutPut
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPaymentOrderStatusOutPut extends ConstructorExpression<PaymentOrderStatusOutPut> {

    private static final long serialVersionUID = -1458576176L;

    public QPaymentOrderStatusOutPut(com.querydsl.core.types.Expression<Long> no, com.querydsl.core.types.Expression<String> orderId, com.querydsl.core.types.Expression<com.payment_service.domain.payment.enums.PaymentOrderStatus> status, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> amount) {
        super(PaymentOrderStatusOutPut.class, new Class<?>[]{long.class, String.class, com.payment_service.domain.payment.enums.PaymentOrderStatus.class, java.math.BigDecimal.class}, no, orderId, status, amount);
    }

}

