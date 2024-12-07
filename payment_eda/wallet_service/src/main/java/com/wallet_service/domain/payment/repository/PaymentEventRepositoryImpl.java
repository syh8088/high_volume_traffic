package com.wallet_service.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wallet_service.domain.payment.model.entity.QPaymentEvent;
import com.wallet_service.domain.payment.model.response.PaymentEventOutPut;
import com.wallet_service.domain.payment.model.response.QPaymentEventOutPut;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PaymentEventRepositoryImpl implements PaymentEventRepositoryCustom {

	QPaymentEvent qPaymentEvent = QPaymentEvent.paymentEvent;

	private final JPAQueryFactory queryFactory;

	public PaymentEventRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<PaymentEventOutPut> selectPaymentEventList() {
		return queryFactory
				.select(
						new QPaymentEventOutPut(
								qPaymentEvent.no,
								qPaymentEvent.paymentKey,
								qPaymentEvent.orderId,
								qPaymentEvent.orderName,
								qPaymentEvent.method,
								qPaymentEvent.type,
								qPaymentEvent.approvedDateTime,
								qPaymentEvent.isPaymentDone
						)
				)
				.from(qPaymentEvent)
				.fetch();
	}

}



