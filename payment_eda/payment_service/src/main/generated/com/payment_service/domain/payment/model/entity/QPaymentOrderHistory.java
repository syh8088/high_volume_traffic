package com.payment_service.domain.payment.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentOrderHistory is a Querydsl query type for PaymentOrderHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOrderHistory extends EntityPathBase<PaymentOrderHistory> {

    private static final long serialVersionUID = -976275015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentOrderHistory paymentOrderHistory = new QPaymentOrderHistory("paymentOrderHistory");

    public final com.payment_service.common.QCommonEntity _super = new com.payment_service.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final EnumPath<com.payment_service.domain.payment.enums.PaymentOrderStatus> newStatus = createEnum("newStatus", com.payment_service.domain.payment.enums.PaymentOrderStatus.class);

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final QPaymentOrder paymentOrder;

    public final EnumPath<com.payment_service.domain.payment.enums.PaymentOrderStatus> previousStatus = createEnum("previousStatus", com.payment_service.domain.payment.enums.PaymentOrderStatus.class);

    public final StringPath reason = createString("reason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public QPaymentOrderHistory(String variable) {
        this(PaymentOrderHistory.class, forVariable(variable), INITS);
    }

    public QPaymentOrderHistory(Path<? extends PaymentOrderHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentOrderHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentOrderHistory(PathMetadata metadata, PathInits inits) {
        this(PaymentOrderHistory.class, metadata, inits);
    }

    public QPaymentOrderHistory(Class<? extends PaymentOrderHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.paymentOrder = inits.isInitialized("paymentOrder") ? new QPaymentOrder(forProperty("paymentOrder"), inits.get("paymentOrder")) : null;
    }

}

