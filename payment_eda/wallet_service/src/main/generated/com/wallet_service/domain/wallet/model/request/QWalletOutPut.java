package com.wallet_service.domain.wallet.model.request;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.wallet_service.domain.wallet.model.request.QWalletOutPut is a Querydsl Projection type for WalletOutPut
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWalletOutPut extends ConstructorExpression<WalletOutPut> {

    private static final long serialVersionUID = 295529115L;

    public QWalletOutPut(com.querydsl.core.types.Expression<Long> walletNo, com.querydsl.core.types.Expression<Long> sellerNo, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> balance) {
        super(WalletOutPut.class, new Class<?>[]{long.class, long.class, java.math.BigDecimal.class}, walletNo, sellerNo, balance);
    }

}

