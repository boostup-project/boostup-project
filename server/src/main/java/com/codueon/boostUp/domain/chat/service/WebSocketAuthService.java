package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.dto.ClaimsVO;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.codueon.boostUp.global.security.utils.AuthConstants.ROLES;

@Service
@RequiredArgsConstructor
public class WebSocketAuthService {
    private final RedisUtils redisUtils;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;
    private final String AUTHORIZATION = "Authorization";

    /**
     * WebSocket authenticate 메서드
     * @param accessor StompHeaderAccessor
     * @author mozzi327
     */
    public void authenticate(StompHeaderAccessor accessor) {
        String getAccessHeader = accessor.getFirstNativeHeader(AUTHORIZATION);
        String accessToken = jwtTokenUtils.parseAccessToken(getAccessHeader);
        if (redisUtils.isBlackList(accessToken) == null) return;
        try {
            Map<String, Object> claims = jwtTokenUtils.getClaims(accessToken);
            Authentication authentication = getAuthentication(claims, accessToken);
            accessor.setUser(authentication);
        } catch (ExpiredJwtException ee) {
            ClaimsVO parseClaims = jwtTokenUtils.parseClaims(accessToken);
            redisUtils.isExistRefreshToken(parseClaims.getSub());
            String generateToken = jwtTokenUtils.generateAccessTokenByClaimsVO(parseClaims);
            List<GrantedAuthority> authorities = authorityUtils.createAuthorities(parseClaims.getRoles());
            Authentication authentication = JwtAuthenticationToken.builder()
                    .credential(null)
                    .id(parseClaims.getId())
                    .name(parseClaims.getName())
                    .principal(parseClaims.getSub())
                    .authorities(authorities)
                    .accessToken(generateToken)
                    .isExpired(true)
                    .build();
            accessor.setUser(authentication);
        }
    }

    /**
     * Authentication 발급 메서드(만료 전)
     * @param claims Claims 정보
     * @param accessToken 액세스 토큰
     * @return Authentication
     * @author mozzi327
     */
    private Authentication getAuthentication(Map<String, Object> claims, String accessToken) {
        List<String> roles = (List<String>) claims.get(ROLES);
        String name = (String) claims.get("name");
        String email = (String) claims.get("email");
        Long memberId = Long.valueOf(claims.get("id").toString());
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);
        return JwtAuthenticationToken.builder()
                .credential(null)
                .id(memberId)
                .name(name)
                .principal(email)
                .authorities(authorities)
                .accessToken(accessToken)
                .isExpired(false)
                .build();
    }

    /**
     * Security Context Authentication 저장 메서드
     * @param authentication Authentication
     * @author mozzi327
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
