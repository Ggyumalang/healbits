package com.zerobase.healbits.challenge.repository;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.common.type.ChallengeCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Slice<Challenge> findAllByChallengeCategoryAndEndDateGreaterThanEqual(ChallengeCategory convertStringToChallengeCategory, LocalDate now, Pageable pageable);
}
