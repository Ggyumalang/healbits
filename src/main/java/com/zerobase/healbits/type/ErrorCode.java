package com.zerobase.healbits.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_COMPLETED_CHALLENGE("이미 완료된 챌린지입니다."),
    INVALID_VERIFICATION_RATE("유효하지 않은 인증률입니다."),
    INVALID_PAYBACK_FEE("유효하지 않은 PAYBACK 금액입니다."),
    CHALLENGE_NOT_FINISHED("종료 전 챌린지입니다."),
    FAILED_CONVERT_VALUE_OF_TYPE("요청하신 값의 타입이 맞지 않습니다."),
    CHALLENGE_STATUS_NOT_FOUND("등록되지 않은 챌린지 상태입니다."),
    ALREADY_FINISHED_CHALLENGE("종료된 챌린지입니다."),
    CHALLENGE_NOT_STARTED("시작 전 챌린지입니다."),
    VERIFICATION_ALREADY_EXIST("금일 인증이 이미 완료되었습니다."),
    FAILED_UPLOAD_FILE("파일 업로드에 실패하였습니다."),
    INVALID_FILE_FORMAT("유효하지 않은 파일 형식입니다."),
    FILE_NOT_EXIST("인증 파일이 존재하지 않습니다."),
    VERIFICATION_USER_NOT_MATCHED("인증 ID가 챌린지 참여한 사용자 ID와 다릅니다."),
    TAKE_CHALLENGE_NOT_FOUND("챌린지에 참여한 기록이 없습니다."),
    ALREADY_PARTICIPATED_CHALLENGE("이미 참여한 챌린지 입니다."),
    INVALID_AMOUNT("잘못된 요청 금액입니다."),
    AMOUNT_EXCEED_BALANCE("결제를 요청하신 포인트는 잔여 포인트를 초과했습니다."),
    INVALID_START_DATE("챌린지 시작일자는 현재 날짜보다 커야합니다."),
    INVALID_END_DATE("챌린지 종료일자는 시작일자보다 커야합니다."),
    CHALLENGE_NOT_FOUND("등록되지 않은 챌린지입니다."),
    HTTP_MESSAGE_NOT_READABLE("요청들어온 형식이 잘못되었습니다."),
    CHALLENGE_CATEGORY_NOT_FOUND("등록되지 않은 챌린지 카테고리입니다."),
    BAD_CREDENTIALS("자격 증명에 실패하였습니다."),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND("등록되지 않은 이메일입니다."),
    TRANSACTION_SERVER_ERROR("트랜잭션 서버 문제입니다."),
    INTERNAL_SERVER_ERROR("내부 서버 문제입니다."),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다.");

    private final String errorMessage;
}
