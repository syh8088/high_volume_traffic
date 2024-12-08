package com.wallet_service.domain.wallet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wallet_service.domain.seller.model.entity.QSeller;
import com.wallet_service.domain.wallet.model.entity.QWallet;
import com.wallet_service.domain.wallet.model.response.QWalletOutPut;
import com.wallet_service.domain.wallet.model.response.WalletOutPut;
import jakarta.persistence.EntityManager;

import java.util.List;

public class WalletRepositoryImpl implements WalletRepositoryCustom {

	QWallet qWallet = QWallet.wallet;
	QSeller qSeller = QSeller.seller;

	private final JPAQueryFactory queryFactory;

	public WalletRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<WalletOutPut> selectWalletListBySellerNoList(List<Long> sellerNoList) {
		return queryFactory
				.select(
						new QWalletOutPut(
								qWallet.no,
								qSeller.no,
								qWallet.balance,
								qWallet.version
						)
				)
				.from(qWallet)
				.innerJoin(qSeller)
				.on(qWallet.seller.eq(qSeller))
				.where(qSeller.no.in(sellerNoList))
				.fetch();
	}

	@Override
	public List<WalletOutPut> selectWalletListByWalletNoList(List<Long> walletNoList) {
		return queryFactory
				.select(
						new QWalletOutPut(
								qWallet.no,
								qSeller.no,
								qWallet.balance,
								qWallet.version
						)
				)
				.from(qWallet)
				.innerJoin(qSeller)
				.on(qWallet.seller.eq(qSeller))
				.where(qWallet.no.in(walletNoList))
				.fetch();
	}
}



