package com.payment_service.domain.product.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.payment_service.domain.product.model.response.QProductOutPut is a Querydsl Projection type for ProductOutPut
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductOutPut extends ConstructorExpression<ProductOutPut> {

    private static final long serialVersionUID = -852618446L;

    public QProductOutPut(com.querydsl.core.types.Expression<Long> productNo, com.querydsl.core.types.Expression<Long> sellerNo, com.querydsl.core.types.Expression<String> productId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> price) {
        super(ProductOutPut.class, new Class<?>[]{long.class, long.class, String.class, String.class, java.math.BigDecimal.class}, productNo, sellerNo, productId, name, price);
    }

}

