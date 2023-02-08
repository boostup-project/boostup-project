package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.vo.SendAlarmMessageEvent;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisChatMessage redisChatMessage;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 메시지 객체(RedisChat) 생성 및 Redis 저장 메서드
     *
     * @param message 메시지 정보
     * @param user    Authentication User 정보
     * @return RedisChat
     * @author mozzi327
     */
    @Transactional
    public void setRedisChatInfo(PostMessage message, JwtAuthenticationToken token) {
        if (token == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        RedisChat createChat = makeRedisChat(
                token.getName(),
                token.getId(),
                message.getChatRoomId(),
                message.getMessageContent());
        createChat.settingCurrentTime();
        redisChatMessage.saveChatMessage(createChat);
        eventPublisher.publishEvent(createChat);
        sendAlarmEvent(message);
    }

    /**
     * 알람 처리 이벤트 메서드
     * @param message 메시지 정보
     * @author mozzi327
     */
    public void sendAlarmEvent(PostMessage message) {
        SendAlarmMessageEvent event = SendAlarmMessageEvent.builder()
                .chatRoomId(message.getChatRoomId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .build();
        eventPublisher.publishEvent(event);
    }

    /**
     * RedisChat 생성 메서드
     *
     * @param name       닉네임(Sender)
     * @param memberId   사용자 식별자(Sender)
     * @param chatRoomId 채팅방 식별자
     * @param content    채팅 내용
     * @return RedisChat
     * @author mozzi327
     */
    private RedisChat makeRedisChat(String name, Long memberId, Long chatRoomId,
                                    String content) {
        return RedisChat.builder()
                .messageType(MessageType.TALK)
                .displayName(name)
                .senderId(memberId)
                .chatRoomId(chatRoomId)
                .message(content)
                .build();
    }

    /**
     * 채팅방 메시지 전체 조회 메서드
     * @param chatRoomId 채팅방 식별자
     * @return List(RedisChat)
     * @author mozzi327
     */
    public List<RedisChat> getChatMessages(JwtAuthenticationToken token, Long chatRoomId) {
        if (token == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        return redisChatMessage.findAll(chatRoomId);
    }
}
