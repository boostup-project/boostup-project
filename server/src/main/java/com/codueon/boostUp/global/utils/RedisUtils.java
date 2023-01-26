package com.codueon.boostUp.global.utils;

import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, String> redisTemplate;
    private final StringRedisTemplate redisEmailTemplate;


    /**
     * redis에 key-value 저장
     * @param key refreshToken
     * @param o String(key)
     * @param minutes 만료시간
     * @author LimJaeminZ
     */

    public void setData(String key, String provider, String o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        String refreshKey = provider + "RT : " + key;
        redisTemplate.opsForValue().set(refreshKey, o, minutes, TimeUnit.MINUTES);
    }
    /**
     * redis에서 key에 대한 value 값 return
     * @param key
     * @return
     * @author LimJaeminZ
     */
    public Object getData(String key, String provider) {
        String refreshKey = provider + "RT : " + key;
        return redisTemplate.opsForValue().get(refreshKey);
    }

    /**
     * redis에서 key에 대한 value 값 delete
     * @param key
     * @param provider
     * @author LimJaeminZ
     */
    public void deleteData(String key, String provider) {
        String refreshKey = provider + "RT : " + key;
        redisTemplate.delete(refreshKey);
    }

    /**
     * redis 로그아웃 처리
     * @param key refreshToken
     * @param o
     * @param setTime 만료시간(분)
     * @author LimJaeminZ
     */
    public void setBlackList(String key, String o, Long setTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, o, setTime, TimeUnit.MILLISECONDS);
    }

    /**
     * accessToken 블랙리스트 여부
     * @param key
     * @return
     * @author LimJaeminZ
     */
    public String isBlackList(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * Redis 이메일 인증 코드 저장 메서드
     * @param key 이메일
     * @param code 인증 코드
     * @author mozzi327
     */
    public void setEmailAuthorizationCode(String key, String code) {
        redisEmailTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(code.getClass()));
        String codeKey = makeCodeKeyForAuthorization(key);
        redisEmailTemplate.opsForValue().set(codeKey, code, 5 * 60L, TimeUnit.SECONDS);
    }

    /**
     * Redis 이메일 인증 코드 반환 메서드
     * @param key 이메일
     * @return String(emailCode)
     * @author mozzi327
     */
    public String getEmailAuthorizationCode(String key) {
        ValueOperations<String, String> stringValueOperations = redisEmailTemplate.opsForValue();
        String codeKey = makeCodeKeyForAuthorization(key);
        Optional<String> emailCode = Optional.ofNullable(stringValueOperations.get(codeKey));
        return emailCode.orElseThrow(() -> new AuthException(ExceptionCode.ALREADY_EXPIRED_EMAIL_CODE));
    }

    /**
     * 이메일 코드 삭제 메서드
     * @param key 이메일
     * @author mozzi327
     */
    public void deleteEmailCode(String key) {
        String codeKey = makeCodeKeyForAuthorization(key);
        redisEmailTemplate.delete(codeKey);
    }

    /**
     * codeKey를 만들어주는 메서드
     * @param key 이메일
     * @return String(codeKey)
     * @author mozzi327
     */
    private String makeCodeKeyForAuthorization(String key) {
        return "EmailCode " + key;
    }

    /**
     * redis refreshToken 존재 유무 메서드
     * @param key 사용자 email
     * @return
     * @author LimJaeminZ
     */
    public void isExistRefreshToken(String key) {
        String refreshKey = "commonRT : " + key;
        Optional<Object> isExistToken = Optional.ofNullable(redisTemplate.opsForValue().get(refreshKey));

        isExistToken.orElseThrow(() -> new AuthException(ExceptionCode.INVALID_REFRESH_TOKEN));
    }
}
