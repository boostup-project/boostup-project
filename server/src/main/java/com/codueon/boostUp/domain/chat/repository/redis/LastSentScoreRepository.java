package com.codueon.boostUp.domain.chat.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class LastSentScoreRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> operations;
    private final Long TTL = 1L;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public void saveLastSentScore(String sessionId, String score) {
        operations.set(sessionId, score);
        redisTemplate.expire(score, TTL, TimeUnit.DAYS);
    }

    public String getLastSentScore(String sessionId) {
        return operations.get(sessionId);
    }

    public String delete(String sessionId) {
        return operations.getAndDelete(sessionId);
    }
}
