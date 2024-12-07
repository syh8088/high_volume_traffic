package com.wallet_service.domain.wallet.service;

import com.wallet_service.domain.wallet.model.entity.Wallet;
import com.wallet_service.domain.wallet.model.request.WalletOutPut;
import com.wallet_service.domain.wallet.repository.WalletRepository;
import com.wallet_service.domain.wallet.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WalletCommandService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public void updateWalletList(List<WalletOutPut> updatedWallets) {
        List<Wallet> wallets = Wallet.of(updatedWallets);
        walletRepository.saveAll(wallets);
    }
}
