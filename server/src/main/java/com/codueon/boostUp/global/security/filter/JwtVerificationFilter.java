package com.codueon.boostUp.global.security.filter;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.domain.member.service.MemberDbService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final RedisUtils redisUtils;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;


    /**
     * JWT 검증 후 Authentication 객체를 SecurityContext에 저장
     * @param request 요청
     * @param response 응답
     * @param filterChain 필터체인
     * @author LimJaeminZ
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Request Header 에서 JWT 토큰 추출
        String accessToken = resolveToken((HttpServletRequest) request);
        if(accessToken != null && redisUtils.isBlackList(accessToken) == null) {
            JwtAuthenticationToken jwtAuthenticationToken = createJwtAuthentication(accessToken);
            if(jwtAuthenticationToken.isExpired()) // 토큰 만료 시
                response.setHeader(AUTHORIZATION, jwtAuthenticationToken.getAccessToken());
            Authentication authentication = jwtAuthenticationToken;
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        doFilter(request, response, filterChain);
    }

    /**
     * Request Header 에서 토큰 정보 추출
     * @param request 요청
     * @return Bearer 제거된 accessToken
     * @author LimJaeminZ
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken == "null") return null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return jwtTokenUtils.parseAccessToken(bearerToken);
        }
        return null;
    }

    /**
     * accessToken 만료 여부 확인 후 JwtAuthenticationToken 객체 반환
     * @param accessToken 엑세스 토큰
     * @return JwtAuthenticationToken
     * @author LimJaeminZ
     */
    private JwtAuthenticationToken createJwtAuthentication(String accessToken) {
        try {
            Map<String, Object> claims = jwtTokenUtils.getClaims(accessToken);
            String stringId = claims.get("id").toString();
            Long id = Long.valueOf(stringId);
            String name = (String) claims.get("name");
            String email = (String) claims.get("sub");
            List<String> roles = (List<String>) claims.get(ROLES);
            List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);
            return JwtAuthenticationToken.builder()
                    .credential(null)
                    .id(id)
                    .name(name)
                    .principal(email)
                    .authorities(authorities)
                    .accessToken(accessToken)
                    .principal(email)
                    .isExpired(false)
                    .build();

        } catch (ExpiredJwtException ee) { // 토큰 만료
            ClaimsVO parseClaims = jwtTokenUtils.parseClaims(accessToken);
            redisUtils.isExistRefreshToken(parseClaims.getSub());
            List<GrantedAuthority> authorities = authorityUtils.createAuthorities(parseClaims.getSub());
            String generateToken = jwtTokenUtils.generateAccessTokenByClaimsVO(parseClaims);

            return JwtAuthenticationToken.builder()
                    .credential(null)
                    .id(parseClaims.getId())
                    .name(parseClaims.getName())
                    .authorities(authorities)
                    .accessToken(generateToken)
                    .principal(parseClaims.getSub())
                    .isExpired(true)
                    .build();
        }
    }
}

