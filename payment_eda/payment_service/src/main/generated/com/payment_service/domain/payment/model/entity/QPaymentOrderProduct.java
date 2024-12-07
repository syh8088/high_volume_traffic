package com.payment_service.domain.payment.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentOrderProduct is a Querydsl query type for PaymentOrderProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOrderProduct extends EntityPathBase<PaymentOrderProduct> {

    private static final long serialVersionUID = 2082284052L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentOrderProduct paymentOrderProduct = new QPaymentOrderProduct("paymentOrderProduct");

    public final com.payment_service.common.QCommonEntity _super = new com.payment_service.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final QPaymentOrder paymentOrder;

    public final QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public QPaymentOrderProduct(String variable) {
        this(PaymentOrderProduct.class, forVariable(variable), INITS);
    }

    public QPaymentOrderProduct(Path<? extends PaymentOrderProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentOrderProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentOrderProduct(PathMetadata metadata, PathInits inits) {
        this(PaymentOrderProduct.class, metadata, inits);
    }

    public QPaymentOrderProduct(Class<? extends PaymentOrderProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.paymentOrder = inits.isInitialized("paymentOrder") ? new QPaymentOrder(forProperty("paymentOrder"), inits.get("paymentOrder")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

