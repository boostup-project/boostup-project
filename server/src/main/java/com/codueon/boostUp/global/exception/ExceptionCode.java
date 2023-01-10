package com.codueon.boostUp.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    // 정보 NOT FOUND
    BOOKMARK_NOT_FOUND(504, "존재하지 않는 북마크입니다."),
    LESSON_NOT_FOUND(504, "존재하지 않는 과외 요약 정보입니다."),
    LESSON_INFO_NOT_FOUND(504, "존재하지 않는 과외 상세 정보입니다."),
    CURRICULUM_NOT_FOUND(504, "존재하지 않는 과외 커리큘럼 정보입니다"),
    MEMBER_NOT_FOUND(504, "존재하지 않는 회원입니다."),
    REVIEW_NOT_FOUND(504, "존재하지 않는 과외 후기입니다."),
    SUGGEST_NOT_FOUND(504, "존재하지 않는 과외 신청 내역입니다."),
    LANGUAGE_NOT_FOUND(504, "존재하지 않는 언어입니다."),
    ADDRESS_NOT_FOUND(504, "존재하지 않는 주소입니다."),
    CHAT_NOT_FOUND(504, "존재하지 않는 메시지입니다."),
    CHATROOM_NOT_FOUND(504, "존재하지 않는 채팅방입니다."),
    RESERVATION_NOT_FOUND(504, "존재하지 않는 예약입니다."),

    // 인증 인가
    INVALID_ACCESS(403, "유효하지 않은 접근입니다."),
    INVALID_MEMBER(403, "회원 정보를 다시 확인하세요."),
    INVALID_OAUTH2(403, "지원하지 않는 OAuth2 프로바이더입니다."),
    INVALID_AUTH_TOKEN(504, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(504, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_ACCESS_TOKEN(504, "엑세스 토큰의 유저 정보가 일치하지 않습니다."),
    BLACK_LIST(504, "로그아웃 처리된 토큰입니다."),
    MISSING_HEADER_ACCESS_TOKEN(504,"헤더에 엑세스 토큰을 넣어주세요."),

    // 중복여부
    EMAIL_ALREADY_EXIST(504, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXIST(504, "이미 존재하는 닉네임입니다."),
    RESOURCE_ALREADY_EXIST(504, "이미 존재하는 데이터입니다."),

    // 요청 실패
    UPLOAD_FAILED(504, "업로드가 실패했습니다."),
    PAYMENT_URL_REQUEST_FAILED(504, "결제 URL 요청을 실패했습니다."),
    TUTOR_CANNOT_RESERVATION(504, "과외 선생님은 자신의 과외를 신청할 수 없습니다."),
    TUTOR_CANNOT_BOOKMARK(504, "과외 선생님은 자신의 과외 북마크를 등록할 수 없습니다."),
    TUTOR_CANNOT_REVIEW(504, "과외 선생님은 자기 과외 리뷰를 남길 수 없습니다."),

    // TODO: 과외 신청 - 결제 관련
    NOT_ACCEPT_SUGGEST(504, "과외 수락 상태가 아닙니다."),
    NOT_PAY_SUCCESS(504, "결제 완료 상태가 아닙니다."),

    // 리뷰 관련
    REVIEW_ONLY_ONE(504, "리뷰는 한 번만 가능합니다."),

    // 이미지 관련
    NO_IMAGE(504, "이미지가 비어있습니다."),
    UNAUTHORIZED_FOR_UPDATE(403, "수정 권한이 없습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
