package com.zerobase.healbits.verification.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVerification is a Querydsl query type for Verification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVerification extends EntityPathBase<Verification> {

    private static final long serialVersionUID = -2029005526L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVerification verification = new QVerification("verification");

    public final com.zerobase.healbits.domain.QBaseEntity _super = new com.zerobase.healbits.domain.QBaseEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> registeredDateTime = _super.registeredDateTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public final DatePath<java.time.LocalDate> verifiedDate = createDate("verifiedDate", java.time.LocalDate.class);

    public final com.zerobase.healbits.takechallenge.domain.QTakeChallenge verifiedTakeChallenge;

    public QVerification(String variable) {
        this(Verification.class, forVariable(variable), INITS);
    }

    public QVerification(Path<? extends Verification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVerification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVerification(PathMetadata metadata, PathInits inits) {
        this(Verification.class, metadata, inits);
    }

    public QVerification(Class<? extends Verification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.verifiedTakeChallenge = inits.isInitialized("verifiedTakeChallenge") ? new com.zerobase.healbits.takechallenge.domain.QTakeChallenge(forProperty("verifiedTakeChallenge"), inits.get("verifiedTakeChallenge")) : null;
    }

}

