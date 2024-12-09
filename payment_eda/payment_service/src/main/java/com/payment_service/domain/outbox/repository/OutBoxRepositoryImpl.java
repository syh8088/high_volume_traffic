package com.payment_service.domain.outbox.repository;

import com.payment_service.domain.outbox.enums.OutBoxStatus;
import com.payment_service.domain.outbox.model.entity.QOutBox;
import com.payment_service.domain.outbox.model.response.OutBoxOutPut;
import com.payment_service.domain.outbox.model.response.QOutBoxOutPut;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OutBoxRepositoryImpl implements OutBoxRepositoryCustom {

	QOutBox qOutBox = QOutBox.outBox;

	private final JPAQueryFactory queryFactory;

	public OutBoxRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}


	@Override
	public List<OutBoxOutPut> selectOutBoxPendingEventMessageList(OutBoxStatus outBoxStatus) {
		return queryFactory
				.select(
						new QOutBoxOutPut(
								qOutBox.no,
								qOutBox.status,
								qOutBox.idempotencyKey,
								qOutBox.type,
								qOutBox.partitionKey,
								qOutBox.payload,
								qOutBox.metadata
						)
				)
				.from(qOutBox)
				.where(qOutBox.status.eq(outBoxStatus))
				.fetch();
	}
}



