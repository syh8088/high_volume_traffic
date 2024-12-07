package com.wallet_service.domain.wallet.repository;

import com.wallet_service.domain.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long>, WalletRepositoryCustom {

}