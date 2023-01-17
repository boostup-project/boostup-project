package com.codueon.boostUp.domain.member.entity;

import lombok.Getter;

import static com.codueon.boostUp.domain.member.utils.OAuthConstant.*;

public enum AccountStatus {
    COMMON_MEMBER("common"),
    KAKAO_MEMBER("kakao"),
    GOOGLE_MEMBER("google"),
    NAVER_MEMBER("naver");

    @Getter
    private final String provider;

    AccountStatus(String provider) {
        this.provider = provider;
    }

    public static AccountStatus whatIsProvider(String provider) {
        switch (provider) {
            case GOOGLE :
                return AccountStatus.GOOGLE_MEMBER;
            case KAKAO :
                return AccountStatus.KAKAO_MEMBER;
            case NAVER :
                return AccountStatus.NAVER_MEMBER;
            default:
//                throw new AuthException(ExceptionCode.INVALID_OAUTH2);
                return AccountStatus.COMMON_MEMBER;
        }
    }
}
