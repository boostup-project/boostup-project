package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final ChannelTopic channelTopic;
    private final RedisChatMessage redisChatMessage;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 메시지 Redis 전송 메서드
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void sendMessage(RedisChat redisChat) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), redisChat);
    }

    /**
     * 입장 메시지 Redis 전송 메서드
     *
     * @param chatRoomId   채팅방 식별자
     * @param senderChat   메시지 정보(Sender)
     * @param receiverChat 메시지 정보(Receiver)
     * @author mozzi327
     */
    public void sendEnterMessage(Long chatRoomId, RedisChat senderChat, RedisChat receiverChat) {
        redisChatMessage.initialMessage(chatRoomId, senderChat, receiverChat);
        redisTemplate.convertAndSend(channelTopic.getTopic(), senderChat);
        redisTemplate.convertAndSend(channelTopic.getTopic(), receiverChat);
    }
}
