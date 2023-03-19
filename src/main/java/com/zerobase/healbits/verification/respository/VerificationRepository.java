package com.zerobase.healbits.verification.respository;

import com.zerobase.healbits.verification.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    List<Verification> findAllByVerifiedTakeChallengeId(long takeChallengeId);

    boolean existsByVerifiedTakeChallengeIdAndVerifiedDate(long takeChallengeId, LocalDate now);
}
