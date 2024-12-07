package com.wallet_service.domain.payment.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.wallet_service.domain.payment.model.response.QPaymentOrderWithSellerOutPut is a Querydsl Projection type for PaymentOrderWithSellerOutPut
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPaymentOrderWithSellerOutPut extends ConstructorExpression<PaymentOrderWithSellerOutPut> {

    private static final long serialVersionUID = 630378102L;

    public QPaymentOrderWithSellerOutPut(com.querydsl.core.types.Expression<Long> paymentOrderNo, com.querydsl.core.types.Expression<String> orderId, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> amount, com.querydsl.core.types.Expression<com.wallet_service.domain.payment.enums.PaymentOrderStatus> status, com.querydsl.core.types.Expression<Long> sellerNo, com.querydsl.core.types.Expression<String> sellerId) {
        super(PaymentOrderWithSellerOutPut.class, new Class<?>[]{long.class, String.class, java.math.BigDecimal.class, com.wallet_service.domain.payment.enums.PaymentOrderStatus.class, long.class, String.class}, paymentOrderNo, orderId, amount, status, sellerNo, sellerId);
    }

}

