package com.payment_service.domain.payment.repository;

import com.payment_service.api.payment.model.response.PaymentOrderOutPut;
import com.payment_service.api.payment.model.response.QPaymentOrderOutPut;
import com.payment_service.domain.payment.model.entity.QPaymentOrder;
import com.payment_service.domain.payment.model.entity.QProduct;
import com.payment_service.domain.payment.model.response.PaymentOrderStatusOutPut;
import com.payment_service.domain.payment.model.response.QPaymentOrderStatusOutPut;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PaymentOrderRepositoryImpl implements PaymentOrderRepositoryCustom {

	QPaymentOrder qPaymentOrder = QPaymentOrder.paymentOrder;
	QProduct qProduct = QProduct.product;

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
}



