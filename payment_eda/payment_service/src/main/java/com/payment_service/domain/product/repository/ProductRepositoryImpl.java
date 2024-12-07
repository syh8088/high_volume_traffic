package com.payment_service.domain.product.repository;

import com.payment_service.domain.payment.model.entity.QProduct;
import com.payment_service.domain.product.model.response.ProductOutPut;
import com.payment_service.domain.product.model.response.QProductOutPut;
import com.payment_service.domain.seller.model.entity.QSeller;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

	QProduct qProduct = QProduct.product;
	QSeller qSeller = QSeller.seller;

	private final JPAQueryFactory queryFactory;

	public ProductRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<ProductOutPut> selectProductListByProductNoList(List<Long> productNoList) {
		return queryFactory
				.select(
						new QProductOutPut(
								qProduct.no,
								qSeller.no,
								qProduct.productId,
								qProduct.name,
								qProduct.price
						)
				)
				.from(qProduct)
				.innerJoin(qSeller)
				.on(qProduct.seller.eq(qSeller))
				.where(qProduct.no.in(productNoList))
				.fetch();
	}
}



