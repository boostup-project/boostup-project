package com.codueon.boostUp.global.security.utils;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@Slf4j
public class JwtTokenUtils {
    @Getter
    private final String secretKey;

    @Getter
    private final int accessTokenExpirationsMinutes;

    @Getter
    private final int refreshTokenExpirationMinutes;


    public JwtTokenUtils(@Value("${jwt.secret-key}") String secretKey,
                         @Value("${jwt.access-token-expiration-minutes}") int accessTokenExpirationsMinutes,
                         @Value("${jwt.refresh-token-expiration-minutes}") int refreshTokenExpirationMinutes) {
        this.secretKey = secretKey;
        this.accessTokenExpirationsMinutes = accessTokenExpirationsMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    /**
     * 엑세스 토큰 발급 메서드
     * @param member
     * @return
     * @author LimJaeminZ
     */
    public String generateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("email", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = getTokenExpiration(getAccessTokenExpirationsMinutes());
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 리프레시 토큰 발급 메서드
     * @param member
     * @return
     * @author LimJaeminZ
     */
    public String generateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = getTokenExpiration(getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 서버 환경변수 시크릿 키를 인코딩하여 변환해주는 메서드
     * @param secretKey 시크릿 키
     * @return 인코딩된 시크릿 키
     * @author LimJaeminZ
     */
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64로 인코딩 된 key -> Key 객체 변환 메서드
     * @param base64EncodedSecretKey base64로 인코딩 된 key
     * @return Key
     * @author LimJaeminZ
     */
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    /**
     * 토큰 만료시간 반환 메서드
     * @param expirationMinutes 서버 저장 엑세서 토큰 만료 시간
     * @return Date
     * @author LimJaeminZ
     */
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    /**
     * 엑세스 토큰 만료 시간 계산
     * @param accessToken
     * @return
     * @author LimJaeminZ
     */
    public Long getExpiration(String accessToken) {
        Key key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));

        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    /**
     * 검증 후 JWS(서명된 JWT) 반환 메서드
     * @param jws
     * @return
     * @author LimJaeminZ
     */
    public Map<String, Object> getClaims(String jws) {
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(jws).getBody();
    }

    /**
     * 엑세스 토큰의 Prefix(Bearer )제거 메서드
     * @param accessToken
     * @return
     * @author LimJaeminZ
     */
    public String parseAccessToken(String accessToken) {
        if(accessToken.startsWith(AuthConstants.BEARER))
            return accessToken.split(" ")[1];
        throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
    }

    /**
     * 토큰 정보를 검증하는 메서드
     * @param token 토큰 정보
     * @return boolean
     * @author LimJaeminZ
     */
    public boolean validateToken(String token) {
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}
