package com.zerobase.healbits.transaction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransaction is a Querydsl query type for Transaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransaction extends EntityPathBase<Transaction> {

    private static final long serialVersionUID = 769741308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransaction transaction = new QTransaction("transaction");

    public final com.zerobase.healbits.domain.QBaseEntity _super = new com.zerobase.healbits.domain.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> balanceSnapshot = createNumber("balanceSnapshot", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zerobase.healbits.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> registeredDateTime = _super.registeredDateTime;

    public final DateTimePath<java.time.LocalDateTime> transactedDateTime = createDateTime("transactedDateTime", java.time.LocalDateTime.class);

    public final StringPath transactionId = createString("transactionId");

    public final EnumPath<com.zerobase.healbits.type.TransactionResultType> transactionResultType = createEnum("transactionResultType", com.zerobase.healbits.type.TransactionResultType.class);

    public final EnumPath<com.zerobase.healbits.type.TransactionType> transactionType = createEnum("transactionType", com.zerobase.healbits.type.TransactionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public QTransaction(String variable) {
        this(Transaction.class, forVariable(variable), INITS);
    }

    public QTransaction(Path<? extends Transaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransaction(PathMetadata metadata, PathInits inits) {
        this(Transaction.class, metadata, inits);
    }

    public QTransaction(Class<? extends Transaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.healbits.member.domain.QMember(forProperty("member")) : null;
    }

}

