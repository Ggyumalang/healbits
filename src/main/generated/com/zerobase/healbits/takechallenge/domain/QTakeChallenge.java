package com.zerobase.healbits.takechallenge.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTakeChallenge is a Querydsl query type for TakeChallenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTakeChallenge extends EntityPathBase<TakeChallenge> {

    private static final long serialVersionUID = -101891300L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTakeChallenge takeChallenge = new QTakeChallenge("takeChallenge");

    public final com.zerobase.healbits.domain.QBaseEntity _super = new com.zerobase.healbits.domain.QBaseEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zerobase.healbits.challenge.domain.QChallenge participatedChallenge;

    public final com.zerobase.healbits.member.domain.QMember participatedMember;

    public final NumberPath<Long> participationFee = createNumber("participationFee", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> registeredDateTime = _super.registeredDateTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public QTakeChallenge(String variable) {
        this(TakeChallenge.class, forVariable(variable), INITS);
    }

    public QTakeChallenge(Path<? extends TakeChallenge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTakeChallenge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTakeChallenge(PathMetadata metadata, PathInits inits) {
        this(TakeChallenge.class, metadata, inits);
    }

    public QTakeChallenge(Class<? extends TakeChallenge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.participatedChallenge = inits.isInitialized("participatedChallenge") ? new com.zerobase.healbits.challenge.domain.QChallenge(forProperty("participatedChallenge"), inits.get("participatedChallenge")) : null;
        this.participatedMember = inits.isInitialized("participatedMember") ? new com.zerobase.healbits.member.domain.QMember(forProperty("participatedMember")) : null;
    }

}

