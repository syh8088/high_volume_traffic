package com.wallet_service.domain.wallet.model.entity;

import com.wallet_service.domain.seller.model.entity.Seller;
import com.wallet_service.domain.wallet.model.response.WalletOutPut;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_no")
    private Seller seller;

    private BigDecimal balance;

    @Version
    private int version;

    @Builder
    private Wallet(Long no, Seller seller, BigDecimal balance, int version) {
        this.no = no;
        this.seller = seller;
        this.balance = balance;
        this.version = version;
    }

    public static Wallet of(long walletNo) {
        return Wallet.builder()
                .no(walletNo)
                .build();
    }

    public static Wallet of(Seller seller, BigDecimal balance) {
        return Wallet.builder()
                .seller(seller)
                .balance(balance)
                .build();
    }

    public static Wallet of(WalletOutPut walletOutPut) {
        return Wallet.builder()
                .no(walletOutPut.getWalletNo())
                .seller(Seller.of(walletOutPut.getSellerNo()))
                .balance(walletOutPut.getBalance())
                .version(walletOutPut.getVersion())
                .build();
    }

    public static List<Wallet> of(List<WalletOutPut> walletOutPutList) {
        return walletOutPutList.stream()
                .map(Wallet::of)
                .toList();
    }
}