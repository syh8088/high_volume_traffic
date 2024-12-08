package com.wallet_service.domain.wallet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wallet_service.domain.wallet.model.entity.QWallet;
import com.wallet_service.domain.wallet.model.entity.QWalletTransaction;
import com.wallet_service.domain.wallet.model.response.QWalletTransactionOutPut;
import com.wallet_service.domain.wallet.model.response.WalletTransactionOutPut;
import jakarta.persistence.EntityManager;

import java.util.List;

public class WalletTransactionRepositoryImpl implements WalletTransactionRepositoryCustom {

	QWallet qWallet = QWallet.wallet;
	QWalletTransaction qWalletTransaction = QWalletTransaction.walletTransaction;

	private final JPAQueryFactory queryFactory;

	public WalletTransactionRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<WalletTransactionOutPut> selectWalletTransactionListByWalletNo(long walletNo) {
		return queryFactory
				.select(
						new QWalletTransactionOutPut(
								qWalletTransaction.amount,
								qWalletTransaction.type,
								qWalletTransaction.orderId,
								qWalletTransaction.referenceType,
								qWalletTransaction.referenceId,
								qWalletTransaction.idempotencyKey
						)
				)
				.from(qWalletTransaction)
				.innerJoin(qWallet)
				.on(qWalletTransaction.wallet.eq(qWallet))
				.where(qWallet.no.eq(walletNo))
				.fetch();

	}
}



