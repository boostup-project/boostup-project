package com.codueon.boostUp.domain.chat.redis;

import com.codueon.boostUp.domain.chat.entity.RedisChat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations operations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate
                    .getStringSerializer()
                    .deserialize(message.getBody());
            RedisChat redisChat = objectMapper.readValue(publishMessage, RedisChat.class);
            operations.convertAndSend("/queue/chat/room/" + redisChat.getRoomId(), redisChat);
            log.info("메시지를 레디스에 받아옵니다.");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
