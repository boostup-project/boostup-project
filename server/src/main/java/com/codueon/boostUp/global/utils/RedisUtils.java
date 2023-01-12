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
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate redisEmailTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    /**
     * redis에 key-value 저장
     *
     * @param key
     * @param o
     * @param minutes
     */
    public void setData(String key, Object o, int minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    /**
     * Redis 이메일 인증 코드 저장 메서드
     * @param key 이메일
     * @param code 인증 코드
     * @param minutes 만료 시간(5분)
     * @author mozzi327
     */
    public void setEmailAuthorizationCode(String key, String code) {
        redisEmailTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(code.getClass()));
        String codeKey = makeCodeKey(key);
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
        String codeKey = makeCodeKey(key);
        Optional<String> emailCode = Optional.ofNullable(stringValueOperations.get(codeKey));
        return emailCode.orElseThrow(() -> new AuthException(ExceptionCode.ALREADY_EXPIRED_EMAIL_CODE));
    }

    /**
     * 이메일 코드 삭제 메서드
     * @param key 이메일
     * @author mozzi327
     */
    public void deleteEmailCode(String key) {
        String codeKey = makeCodeKey(key);
        redisEmailTemplate.delete(codeKey);
    }

    /**
     * codeKey를 만들어주는 메서드
     * @param key 이메일
     * @return String(codeKey)
     * @author mozzi327
     */
    private String makeCodeKey(String key) {
        return "EmailCode " + key;
    }

    /**
     * redis에서 key에 대한 value 값 return
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
