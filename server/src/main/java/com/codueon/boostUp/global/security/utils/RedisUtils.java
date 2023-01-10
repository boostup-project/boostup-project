package com.codueon.boostUp.global.security.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
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
     * redis에서 key에 대한 value 값 return
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
