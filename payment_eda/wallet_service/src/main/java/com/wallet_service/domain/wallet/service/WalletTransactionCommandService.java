package com.wallet_service.domain.wallet.service;

import com.wallet_service.domain.wallet.model.entity.WalletTransaction;
import com.wallet_service.domain.wallet.model.request.WalletOutPut;
import com.wallet_service.domain.wallet.model.request.WalletTransactionOutPut;
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
public class WalletTransactionCommandService {

    private final WalletTransactionRepository walletTransactionRepository;

    public void insertWalletTransactionList(List<WalletOutPut> updatedWallets) {

        List<WalletTransaction> walletTransactions = updatedWallets.stream()
                .map(data -> {
                    List<WalletTransactionOutPut> walletTransactionList = data.getWalletTransactionList();
                    return WalletTransaction.of(data.getWalletNo(), walletTransactionList);
                })
                .flatMap(List::stream)
                .toList();

        walletTransactionRepository.saveAll(walletTransactions);
    }
}
