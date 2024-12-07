package com.wallet_service.domain.wallet.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionType {

    CREDIT("CREDIT"),
    DEBIT("DEBIT")

    ;

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getTransactionType() {
        return this.type;
    }

    public static TransactionType getByTransactionType(String type) {
        return Arrays.stream(TransactionType.values())
                .filter(data -> data.getTransactionType().equals(type))
                .findFirst()
                .orElse(null);
    }
}
