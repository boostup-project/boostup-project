package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChannelTopic channelTopic;
    private final RedisChatMessage redisChatMessage;
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 메시지 객체(RedisChat) 생성 및 Redis 저장 메서드
     * @param message 메시지 정보
     * @param user Authentication User 정보
     * @return RedisChat
     * @author mozzi327
     */
    @Transactional
    public RedisChat setRedisChatInfo(PostMessage message, JwtAuthenticationToken token) {
        if (token == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        RedisChat createChat = RedisChat.builder()
                .messageType(MessageType.TALK)
                .displayName(token.getName())
                .senderId(token.getId())
                .chatRoomId(message.getChatRoomId())
                .message(message.getMessageContent())
                .build();
        createChat.settingCurrentTime();
        redisChatMessage.saveChatMessage(createChat);
        return createChat;
    }

    public List<RedisChat> getChatMessages(Long chatRoomId) {
        return redisChatMessage.findAll(chatRoomId);
    }

    /**
     * 입장 메시지 Redis 전송 메서드
     * @param chatRoomId 채팅방 식별자
     * @param memberId 사용자 식별자
     * @param displayName 닉네임
     * @author mozzi327
     */
    public void sendEnterMessage(Long chatRoomId, RedisChat senderChat, RedisChat receiverChat) {
        redisChatMessage.initialMessage(chatRoomId, senderChat, receiverChat);
        redisTemplate.convertAndSend(channelTopic.getTopic(), senderChat);
        redisTemplate.convertAndSend(channelTopic.getTopic(), receiverChat);
    }
}
