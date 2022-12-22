package com.codueon.boostUp.domain.member.entity;

import static com.codueon.boostUp.domain.member.utils.OAuthConstant.*;

public enum AccountStatus {
    COMMON_MEMBER, KAKAO_MEMBER, GOOGLE_MEMBER, NAVER_MEMBER;

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
