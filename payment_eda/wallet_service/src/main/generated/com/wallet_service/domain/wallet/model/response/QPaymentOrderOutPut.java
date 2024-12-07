package com.wallet_service.domain.wallet.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.wallet_service.domain.wallet.model.response.QPaymentOrderOutPut is a Querydsl Projection type for PaymentOrderOutPut
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPaymentOrderOutPut extends ConstructorExpression<PaymentOrderOutPut> {

    private static final long serialVersionUID = 346199380L;

    public QPaymentOrderOutPut(com.querydsl.core.types.Expression<Long> paymentOrderNo, com.querydsl.core.types.Expression<String> orderId, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> amount, com.querydsl.core.types.Expression<com.wallet_service.domain.payment.enums.PaymentOrderStatus> status, com.querydsl.core.types.Expression<Long> productNo, com.querydsl.core.types.Expression<String> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> price) {
        super(PaymentOrderOutPut.class, new Class<?>[]{long.class, String.class, java.math.BigDecimal.class, com.wallet_service.domain.payment.enums.PaymentOrderStatus.class, long.class, String.class, String.class, java.math.BigDecimal.class}, paymentOrderNo, orderId, amount, status, productNo, productId, name, price);
    }

}

