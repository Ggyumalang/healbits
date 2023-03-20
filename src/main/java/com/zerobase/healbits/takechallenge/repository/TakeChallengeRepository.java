package com.zerobase.healbits.takechallenge.repository;

import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeChallengeRepository extends JpaRepository<TakeChallenge, Long>, TakeChallengeRepositoryCustom {
    boolean existsByParticipatedChallengeIdAndParticipatedMemberId(long challengeId, long memberId);

}
