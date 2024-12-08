package com.wallet_service.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {


	private final JPAQueryFactory queryFactory;

	public ProductRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

}



