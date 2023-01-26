package com.codueon.boostUp.global.security.provider;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.dto.ClaimsVO;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.codueon.boostUp.global.security.utils.AuthConstants.ROLES;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;


    /**
     * JwtAuthenticationToken 발급 메서드
     * @param authentication the authentication request object.
     * @return
     * @author LimJaeminZ
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        String accessToken = jwtToken.getAccessToken();
        Map<String, Object> claims = jwtTokenUtils.getClaims(jwtToken.getAccessToken());
        String stringId = claims.get("id").toString();
        Long id = Long.valueOf(stringId);
        String email = (String) claims.get("email");
        List<String> roles = (List<String>) claims.get(ROLES);
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);

        return new JwtAuthenticationToken(authorities, email, null, id, false, accessToken);
    }

    /**
     * 토큰 타입이 일치하는지 검증하는 메서드
     * @param authentication
     * @return
     * @author LimJaeminZ
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
