package com.zerobase.healbits.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_FOUND("등록되지 않은 이메일입니다."),
    INTERNAL_SERVER_ERROR("내부 서버 문제입니다."),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다."),
    PASSWORD_IS_EMPTY("비밀번호가 빈칸입니다."),
    HASHED_PASSWORD_IS_EMPTY("해쉬된 비밀번호가 빈칸입니다.");

    private final String errorMessage;
}
