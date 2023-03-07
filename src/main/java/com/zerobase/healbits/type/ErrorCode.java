package com.zerobase.healbits.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    START_DATE_INVALID("챌린지 시작일자는 현재 날짜보다 커야합니다."),
    END_DATE_INVALID("챌린지 종료일자는 시작일자보다 커야합니다."),
    CHALLENGE_NOT_FOUND("등록되지 않은 챌린지입니다."),
    WRONG_DATE_FORMAT("잘못된 날짜 형식입니다."),
    CHALLENGE_CATEGORY_NOT_FOUND("등록되지 않은 챌린지 카테고리입니다."),
    WRONG_PASSWORD_ERROR("비밀번호가 틀렸습니다."),
    WRONG_PHONE_FORMAT("잘못된 휴대폰 번호입니다."),
    EMAIL_NOT_FOUND("등록되지 않은 이메일입니다."),
    INTERNAL_SERVER_ERROR("내부 서버 문제입니다."),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다."),
    PASSWORD_IS_EMPTY("비밀번호가 빈칸입니다."),
    HASHED_PASSWORD_IS_EMPTY("해쉬된 비밀번호가 빈칸입니다.");

    private final String errorMessage;
}
