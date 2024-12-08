package com.wallet_service.domain.wallet.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.wallet.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class WalletTransactionOutPut {

    private BigDecimal amount;
    private TransactionType type;
    private String orderId;
    private String referenceType;
    private Long referenceId;
    private String idempotencyKey;

    @QueryProjection
    public WalletTransactionOutPut(BigDecimal amount, TransactionType type, String orderId, String referenceType, Long referenceId, String idempotencyKey) {
        this.amount = amount;
        this.type = type;
        this.orderId = orderId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.idempotencyKey = idempotencyKey;
    }

    @Builder
    private WalletTransactionOutPut(BigDecimal amount, TransactionType type, String orderId, String referenceType, Long referenceId) {
        this.amount = amount;
        this.type = type;
        this.orderId = orderId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    public static WalletTransactionOutPut of(
            BigDecimal amount,
            TransactionType type,
            String orderId,
            String referenceType,
            Long referenceId
    ) {
        return WalletTransactionOutPut.builder()
                .amount(amount)
                .type(type)
                .orderId(orderId)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();
    }
}
