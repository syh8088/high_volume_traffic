package com.wallet_service.domain.wallet.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.wallet_service.domain.payment.model.response.PaymentOrderWithSellerOutPut;
import com.wallet_service.domain.wallet.enums.ReferenceType;
import com.wallet_service.domain.wallet.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class WalletOutPut {

    private long walletNo;
    private long sellerNo;
    private BigDecimal balance;
    private int version;
    private List<WalletTransactionOutPut> walletTransactionList;

    @QueryProjection
    public WalletOutPut(long walletNo, long sellerNo, BigDecimal balance, int version) {
        this.walletNo = walletNo;
        this.sellerNo = sellerNo;
        this.balance = balance;
        this.version = version;
    }

    public void calculateBalance(List<PaymentOrderWithSellerOutPut> paymentOrders) {

        double calculatedAmount = paymentOrders.stream()
                .mapToDouble(data -> data.getAmount().doubleValue())
                .sum();

        BigDecimal calculatedBalance = BigDecimal.valueOf(calculatedAmount);
        this.updateBalance(calculatedBalance.add(this.balance));

        List<WalletTransactionOutPut> walletTransactionList = paymentOrders.stream()
                .map(item -> WalletTransactionOutPut.of(
                        item.getAmount(),
                        TransactionType.CREDIT,
                        item.getOrderId(),
                        ReferenceType.PAYMENT_ORDER.name(),
                        item.getSellerNo()
                ))
                .toList();

        this.updateWalletTransactionList(walletTransactionList);
    }

    private void updateBalance(BigDecimal calculatedBalance) {
        this.balance = calculatedBalance;
    }

    private void updateWalletTransactionList(List<WalletTransactionOutPut> walletTransactionList) {
        this.walletTransactionList = walletTransactionList;
    }

    public void addBalance(WalletOutPut wallet) {

        this.balance = BigDecimal.valueOf(
                wallet.getWalletTransactionList().stream()
                        .mapToDouble(data -> data.getAmount().doubleValue())
                        .sum()
        ).add(this.balance);
    }

    public void updateWalletTransactions(List<WalletTransactionOutPut> walletTransactionOutPuts) {
        this.walletTransactionList = walletTransactionOutPuts;
    }
}
