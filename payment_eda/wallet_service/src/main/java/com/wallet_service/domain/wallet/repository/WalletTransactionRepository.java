package com.wallet_service.domain.wallet.repository;

import com.wallet_service.domain.wallet.model.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    boolean existsByOrderId(String orderId);
}