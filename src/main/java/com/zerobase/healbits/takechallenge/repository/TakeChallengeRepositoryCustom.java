package com.zerobase.healbits.takechallenge.repository;

import com.zerobase.healbits.takechallenge.dto.TakeChallengeInfo;

import java.time.LocalDate;
import java.util.List;

public interface TakeChallengeRepositoryCustom {
    List<TakeChallengeInfo> findAllByParticipatedMemberIdAndReady(
            long participatedMemberId, LocalDate now
    );

    List<TakeChallengeInfo> findAllByParticipatedMemberIdAndInProgress(
            long participatedMemberId, LocalDate now
    );

    List<TakeChallengeInfo> findAllByParticipatedMemberIdAndClosed(
            long participatedMemberId, LocalDate now
    );
}
