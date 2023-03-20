package com.zerobase.healbits.takechallenge.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeInfo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.zerobase.healbits.challenge.domain.QChallenge.challenge;
import static com.zerobase.healbits.takechallenge.domain.QTakeChallenge.takeChallenge;

@RequiredArgsConstructor
public class TakeChallengeRepositoryCustomImpl implements TakeChallengeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<TakeChallengeInfo> findAllByParticipatedMemberIdAndReady(long participatedMemberId, LocalDate now) {
        return queryFactory.select(Projections.bean(TakeChallengeInfo.class
                        , challenge.challengeName
                        , challenge.summary
                        , challenge.startDate
                        , challenge.endDate))
                .from(takeChallenge)
                .innerJoin(challenge).on(takeChallenge.participatedChallenge.id.eq(challenge.id))
                .where(takeChallenge.participatedMember.id.eq(participatedMemberId),
                        challenge.startDate.after(now))
                .fetch();
    }

    @Override
    public List<TakeChallengeInfo> findAllByParticipatedMemberIdAndInProgress(long participatedMemberId, LocalDate now) {
        return queryFactory.select(Projections.bean(TakeChallengeInfo.class
                        , challenge.challengeName
                        , challenge.summary
                        , challenge.startDate
                        , challenge.endDate))
                .from(takeChallenge)
                .innerJoin(challenge).on(takeChallenge.participatedChallenge.id.eq(challenge.id))
                .where(takeChallenge.participatedMember.id.eq(participatedMemberId),
                        challenge.startDate.loe(now),
                        challenge.endDate.goe(now))
                .fetch();
    }

    @Override
    public List<TakeChallengeInfo> findAllByParticipatedMemberIdAndClosed(long participatedMemberId, LocalDate now) {
        return queryFactory.select(Projections.bean(TakeChallengeInfo.class
                        , challenge.challengeName
                        , challenge.summary
                        , challenge.startDate
                        , challenge.endDate))
                .from(takeChallenge)
                .innerJoin(challenge).on(takeChallenge.participatedChallenge.id.eq(challenge.id))
                .where(takeChallenge.participatedMember.id.eq(participatedMemberId),
                        challenge.endDate.before(now))
                .fetch();
    }
}
