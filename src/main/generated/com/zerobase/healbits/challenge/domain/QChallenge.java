package com.zerobase.healbits.challenge.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = -1181167076L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final com.zerobase.healbits.common.domain.QBaseEntity _super = new com.zerobase.healbits.common.domain.QBaseEntity(this);

    public final EnumPath<com.zerobase.healbits.common.type.ChallengeCategory> challengeCategory = createEnum("challengeCategory", com.zerobase.healbits.common.type.ChallengeCategory.class);

    public final StringPath challengeName = createString("challengeName");

    public final StringPath contents = createString("contents");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> participantsNum = createNumber("participantsNum", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> registeredDateTime = _super.registeredDateTime;

    public final com.zerobase.healbits.member.domain.QMember registeredMember;

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath summary = createString("summary");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDateTime = _super.updatedDateTime;

    public QChallenge(String variable) {
        this(Challenge.class, forVariable(variable), INITS);
    }

    public QChallenge(Path<? extends Challenge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallenge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallenge(PathMetadata metadata, PathInits inits) {
        this(Challenge.class, metadata, inits);
    }

    public QChallenge(Class<? extends Challenge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.registeredMember = inits.isInitialized("registeredMember") ? new com.zerobase.healbits.member.domain.QMember(forProperty("registeredMember")) : null;
    }

}

