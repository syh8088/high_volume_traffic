package com.wallet_service.domain.wallet.model.entity;

import com.wallet_service.domain.wallet.enums.TransactionType;
import com.wallet_service.domain.wallet.model.response.WalletTransactionOutPut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wallet_transactions")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_no")
    private Wallet wallet;

    @Column
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    private WalletTransaction(Wallet wallet, BigDecimal amount, TransactionType type, String referenceType, long referenceId, String orderId, String idempotencyKey) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
    }

    public static WalletTransaction of(Wallet wallet, BigDecimal amount, TransactionType type, String referenceType, long referenceId, String orderId, String idempotencyKey) {
        return new WalletTransaction(wallet, amount, type, referenceType, referenceId, orderId, idempotencyKey);
    }

    public static WalletTransaction of(long walletNo, BigDecimal amount, TransactionType type, String referenceType, long referenceId, String orderId, String idempotencyKey) {
        return new WalletTransaction(Wallet.of(walletNo), amount, type, referenceType, referenceId, orderId, idempotencyKey);
    }

    public static List<WalletTransaction> of(long walletNo, List<WalletTransactionOutPut> walletTransactionList) {
        return walletTransactionList.stream()
                .map(data -> WalletTransaction.of(
                        walletNo,
                        data.getAmount(),
                        data.getType(),
                        data.getReferenceType(),
                        data.getReferenceId(),
                        data.getOrderId(),
                        data.getIdempotencyKey()
                ))
                .toList();
    }
}