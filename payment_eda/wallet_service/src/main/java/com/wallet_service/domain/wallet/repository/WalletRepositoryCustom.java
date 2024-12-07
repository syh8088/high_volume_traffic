package com.wallet_service.domain.wallet.repository;

import com.wallet_service.domain.wallet.model.request.WalletOutPut;

import java.util.List;

public interface WalletRepositoryCustom {


    List<WalletOutPut> selectWalletListBySellerNoList(List<Long> sellerNoList);
}
