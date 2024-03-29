package com.codueon.boostUp.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    // 정보 NOT FOUND
    BOOKMARK_NOT_FOUND(415, "존재하지 않는 북마크입니다."),
    LESSON_NOT_FOUND(415, "존재하지 않는 과외 요약 정보입니다."),
    LESSON_INFO_NOT_FOUND(415, "존재하지 않는 과외 상세 정보입니다."),
    CURRICULUM_NOT_FOUND(415, "존재하지 않는 과외 커리큘럼 정보입니다"),
    MEMBER_NOT_FOUND(415, "존재하지 않는 회원입니다."),
    REVIEW_NOT_FOUND(415, "존재하지 않는 과외 후기입니다."),
    SUGGEST_NOT_FOUND(415, "존재하지 않는 과외 신청 내역입니다."),
    LANGUAGE_NOT_FOUND(415, "존재하지 않는 언어입니다."),
    ADDRESS_NOT_FOUND(415, "존재하지 않는 주소입니다."),
    STATUS_NOT_FOUND(415, "존재하지 않는 결제 상태입니다."),
    CHAT_NOT_FOUND(415, "존재하지 않는 메시지입니다."),
    CHATROOM_NOT_FOUND(415, "존재하지 않는 채팅방입니다."),
    EMAIL_NOT_FOUND(415, "이메일 정보를 다시 확인해주세요."),



    // 인증 인가
    UNAUTHORIZED_FOR_UPDATE(403, "수정 권한이 없습니다."),
    UNAUTHORIZED_FOR_DELETE(403, "삭제 권한이 없습니다."),
    INVALID_ACCESS(403, "유효하지 않은 접근입니다."),
    INVALID_MEMBER(403, "회원 정보를 다시 확인하세요."),
    INVALID_OAUTH2(403, "지원하지 않는 OAuth2 프로바이더입니다."),
    INVALID_AUTH_TOKEN(403, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(403, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_ACCESS_TOKEN(403, "엑세스 토큰의 유저 정보가 일치하지 않습니다."),
    BLACK_LIST(403, "로그아웃 처리된 토큰입니다."),
    MISSING_HEADER_ACCESS_TOKEN(403,"토큰이 존재하지 않습니다."),
    NO_AUTHORIZATION_EDIT_LESSON(403, "요약 정보를 수정할 권한이 없습니다."),
    NO_AUTHORIZATION_EDIT_LESSON_INFO(403, "상세 정보를 수정할 권한이 없습니다."),
    NO_AUTHORIZATION_EDIT_CURRICULUM(403, "커리큘럼을 수정할 권한이 없습니다."),
    BOOKMARK_REQUIRED_LOGIN(403, "로그인이 필요한 서비스입니다."),

    // 중복여부
    EMAIL_ALREADY_EXIST(504, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXIST(504, "이미 존재하는 닉네임입니다."),
    RESOURCE_ALREADY_EXIST(504, "이미 존재하는 데이터입니다."),
    LESSON_ALREADY_EXIST(504, "한 회원 당 하나의 과외만 등록 가능합니다."),
    SUGGEST_ALREADY_EXIST(504, "한 과외 당 하나의 신청만 가능합니다."),
    PURCHASE_ALREADY_EXIST(504, "한 과외 당 하나의 이용권 구매만 가능합니다."),
    // 요청 실패
    UPLOAD_FAILED(504, "업로드가 실패했습니다."),
    PAYMENT_URL_REQUEST_FAILED(504, "결제 URL 요청을 실패했습니다."),
    TUTOR_CANNOT_RESERVATION(504, "과외 선생님은 자신의 과외를 신청할 수 없습니다."),
    TUTOR_CANNOT_BOOKMARK(504, "과외 선생님은 자신의 과외 북마크를 등록할 수 없습니다."),
    TUTOR_CANNOT_REVIEW(504, "과외 선생님은 자기 과외 리뷰를 남길 수 없습니다."),
    // 웹소켓
    INVALID_DESTINATION(504, "매칭되는 채팅방이 존재하지 않습니다."),
    INVALID_CHAT_ROOM_ID(504, "유효하지 않은 채팅방 식별자입니다."),
    INVALID_CHAT_ROOM_MEMBER(504, "채팅방 인원 수는 최대 두 명입니다."),
    FAIL_TO_SERIALIZE(504, "잘못된 JSON 정보입니다."),
    CHATROOM_ALREADY_EXIST(504, "채팅방이 이미 존재합니다."),
    NO_CHAT_IN_CACHE_EXIST(504, "메시지 캐시가 존재하지 않습니다."),

    // 인증 코드
    INVALID_EMAIL_CODE(403, "유효하지 않은 인증 코드입니다."),
    ALREADY_EXPIRED_EMAIL_CODE(403, "인증 코드가 만료되었습니다."),
    IMPOSSIBLE_CHANGE_SAME_PASSWORD(504, "같은 비밀번호로는 변경할 수 없습니다."),

    // 과외 신청
    NOT_ACCEPT_IN_PROGRESS(504, "과외 수락 대기중 상태가 아닙니다."),
    NOT_PAY_IN_PROGRESS(504, "결제 대기중 상태가 아닙니다."),
    NOT_PAY_SUCCESS(504, "결제 완료 상태가 아닙니다."),
    NOT_END_OF_LESSON(504, "과외 종료 상태가 아닙니다."),
    NOT_DURING_LESSON(504, "과외 중 상태가 아닙니다."),
    NOT_REFUND_PAYMENT(504, "환불 완료 상태가 아닙니다."),
    NOT_SUGGEST_OR_NOT_ACCEPT(504, "과외 수락 대기중 또는 결제 대기중 상태가 아닙니다."),

    // 리뷰 관련
    REVIEW_ONLY_ONE(504, "리뷰는 한 번만 가능합니다."),

    // 이미지 관련
    NO_IMAGE(504, "이미지가 비어있습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
