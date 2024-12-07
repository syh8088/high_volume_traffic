package com.wallet_service.domain.wallet.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWalletTransaction is a Querydsl query type for WalletTransaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalletTransaction extends EntityPathBase<WalletTransaction> {

    private static final long serialVersionUID = -1398596100L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWalletTransaction walletTransaction = new QWalletTransaction("walletTransaction");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath idempotencyKey = createString("idempotencyKey");

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final StringPath orderId = createString("orderId");

    public final NumberPath<Long> referenceId = createNumber("referenceId", Long.class);

    public final StringPath referenceType = createString("referenceType");

    public final EnumPath<com.wallet_service.domain.wallet.enums.TransactionType> type = createEnum("type", com.wallet_service.domain.wallet.enums.TransactionType.class);

    public final QWallet wallet;

    public QWalletTransaction(String variable) {
        this(WalletTransaction.class, forVariable(variable), INITS);
    }

    public QWalletTransaction(Path<? extends WalletTransaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWalletTransaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWalletTransaction(PathMetadata metadata, PathInits inits) {
        this(WalletTransaction.class, metadata, inits);
    }

    public QWalletTransaction(Class<? extends WalletTransaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wallet = inits.isInitialized("wallet") ? new QWallet(forProperty("wallet"), inits.get("wallet")) : null;
    }

}

