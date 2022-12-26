package com.codueon.boostUp.domain.chat.redis;

import com.codueon.boostUp.domain.chat.entity.RedisChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publishingTopic(ChannelTopic topic, RedisChat message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
        log.info("레디스 송신 완료");
    }
}
