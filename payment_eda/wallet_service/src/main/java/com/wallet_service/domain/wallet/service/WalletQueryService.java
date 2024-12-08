package com.wallet_service.domain.wallet.service;

import com.wallet_service.domain.wallet.model.response.WalletOutPut;
import com.wallet_service.domain.wallet.model.response.WalletTransactionOutPut;
import com.wallet_service.domain.wallet.repository.WalletRepository;
import com.wallet_service.domain.wallet.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public boolean isAlreadyProcessWallet(String orderId) {

        return walletTransactionRepository.existsByOrderId(orderId);
    }

    public List<WalletOutPut> selectWalletListBySellerNoList(List<Long> sellerNoList) {
        return walletRepository.selectWalletListBySellerNoList(sellerNoList);
    }

    public List<WalletOutPut> selectWalletListWithWalletTransactionsByWalletNoList(List<Long> requestWalletNoList) {

        List<WalletOutPut> walletOutPuts = walletRepository.selectWalletListByWalletNoList(requestWalletNoList);

//        for (WalletOutPut wallet : walletOutPuts) {
//            long walletNo = wallet.getWalletNo();
//            List<WalletTransactionOutPut> walletTransactionOutPuts
//                    = walletTransactionRepository.selectWalletTransactionListByWalletNo(walletNo);
//
//            wallet.updateWalletTransactions(walletTransactionOutPuts);
//        }

      return walletOutPuts;
    }
}
