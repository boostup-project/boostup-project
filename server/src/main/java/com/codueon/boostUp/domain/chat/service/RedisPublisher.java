package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
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

    public void publishingTopic(ChannelTopic topic, RedisChat redisChat) {
        redisTemplate.convertAndSend(topic.getTopic(), redisChat);
        log.info("레디스 송신 완료");
    }
}
