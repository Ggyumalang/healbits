package com.zerobase.healbits.type;

import com.zerobase.healbits.exception.HealBitsException;

import static com.zerobase.healbits.type.ErrorCode.CHALLENGE_CATEGORY_NOT_FOUND;

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
            throw new HealBitsException(CHALLENGE_CATEGORY_NOT_FOUND);
        }
    }
}
