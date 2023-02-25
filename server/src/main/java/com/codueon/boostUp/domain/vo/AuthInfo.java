package com.codueon.boostUp.domain.vo;

import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import java.security.Principal;

@Getter
@NoArgsConstructor
public class AuthInfo {
    private Long memberId;
    private String name;
    private String email;

    @Builder
    public AuthInfo(Long memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public static AuthInfo of(Authentication auth) {
        if (auth == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return AuthInfo.builder()
                .memberId(token.getId())
                .name(token.getName())
                .email((String) token.getPrincipal())
                .build();
    }

    public static AuthInfo of(Principal principal) {
        if (principal == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return AuthInfo.builder()
                .memberId(token.getId())
                .name(token.getName())
                .email((String) token.getPrincipal())
                .build();
    }

    public static AuthInfo ofNotRequiredToken(Authentication auth) {
        if (auth == null) return null;
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return AuthInfo.builder()
                .memberId(token.getId())
                .name(token.getName())
                .email((String) token.getPrincipal())
                .build();
    }

    public static Long ofMemberId(Authentication auth) {
        if (auth == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return token.getId();
    }

    public static Long ofMemberIdNotRequired(Authentication auth) {
        if (auth == null) return null;
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return token.getId();
    }

    public static JwtAuthenticationToken convertToToken(Authentication auth) {
        if (auth == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return token;
    }
}
