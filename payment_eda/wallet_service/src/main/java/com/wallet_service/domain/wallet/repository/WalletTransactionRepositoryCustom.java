package com.wallet_service.domain.wallet.repository;

import com.wallet_service.domain.wallet.model.response.WalletTransactionOutPut;

import java.util.List;

public interface WalletTransactionRepositoryCustom {

    List<WalletTransactionOutPut> selectWalletTransactionListByWalletNo(long walletNo);
}
