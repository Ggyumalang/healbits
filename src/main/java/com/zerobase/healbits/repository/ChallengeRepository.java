package com.zerobase.healbits.repository;

import com.zerobase.healbits.domain.Challenge;
import com.zerobase.healbits.type.ChallengeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByChallengeCategoryAndEndDateGreaterThanEqual(ChallengeCategory convertStringToChallengeCategory, LocalDate now);
}
