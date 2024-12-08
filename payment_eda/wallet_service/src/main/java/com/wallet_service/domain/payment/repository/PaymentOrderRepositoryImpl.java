package com.wallet_service.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wallet_service.domain.payment.model.entity.QPaymentOrder;
import com.wallet_service.domain.payment.model.response.*;
import com.wallet_service.domain.product.model.entity.QProduct;
import com.wallet_service.domain.seller.model.entity.QSeller;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PaymentOrderRepositoryImpl implements PaymentOrderRepositoryCustom {

	QPaymentOrder qPaymentOrder = QPaymentOrder.paymentOrder;
	QProduct qProduct = QProduct.product;
	QSeller qSeller = QSeller.seller;

	private final JPAQueryFactory queryFactory;

	public PaymentOrderRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<PaymentOrderStatusOutPut> selectPaymentOrderStatusListByOrderId(String orderId) {
		return queryFactory
				.select(
						new QPaymentOrderStatusOutPut(
								qPaymentOrder.no,
								qPaymentOrder.orderId,
								qPaymentOrder.status,
								qPaymentOrder.amount
						)
				)
				.from(qPaymentOrder)
				.where(qPaymentOrder.orderId.eq(orderId))
				.fetch();
	}

	@Override
	public List<PaymentOrderOutPut> selectPaymentOrderListWithProductByOrderIdList(List<String> orderIdList) {
		return queryFactory
				.select(
						new QPaymentOrderOutPut(
								qPaymentOrder.no,
								qPaymentOrder.orderId,
								qPaymentOrder.amount,
								qPaymentOrder.status,
								qProduct.no,
								qProduct.productId,
								qProduct.name,
								qProduct.price
						)
				)
				.from(qPaymentOrder)
				.innerJoin(qProduct)
				.on(qPaymentOrder.product.eq(qProduct))
				.where(qPaymentOrder.orderId.in(orderIdList))
				.fetch();
	}

	@Override
	public List<PaymentOrderWithSellerOutPut> selectPaymentOrderListWithSellerByOrderId(String orderId) {
		return queryFactory
				.select(
						new QPaymentOrderWithSellerOutPut(
								qPaymentOrder.no,
								qPaymentOrder.orderId,
								qPaymentOrder.amount,
								qPaymentOrder.status,
								qSeller.no,
								qSeller.sellerId
						)
				)
				.from(qPaymentOrder)
				.innerJoin(qSeller)
				.on(qPaymentOrder.seller.eq(qSeller))
				.where(qPaymentOrder.orderId.eq(orderId))
				.fetch();
	}
}



