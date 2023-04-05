package com.zerobase.healbits.common.type;

import com.zerobase.healbits.common.exception.HealBitsException;

public enum ChallengeCategory {
    HEALTH,
    LIFE,
    HOBBY,
    ENVIRONMENT,
    EATING_HABITS;
    public static ChallengeCategory convertStringToChallengeCategory(String challengeCategory) {
        try {
            return ChallengeCategory.valueOf(challengeCategory);
        } catch (IllegalArgumentException e) {
            throw new HealBitsException(ErrorCode.CHALLENGE_CATEGORY_NOT_FOUND);
        }
    }
}
