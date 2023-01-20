package com.codueon.chatserver.domain.chat.controller;

import com.codueon.chatserver.domain.chat.dto.PostMessage;
import com.codueon.chatserver.domain.chat.dto.RedisChat;
import com.codueon.chatserver.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @MessageMapping("/rooms")
    public void sendMessage(PostMessage message, StompHeaderAccessor headerAccessor) {
        log.info("[SEND] start {}", headerAccessor.getSessionId());
        RedisChat convertedRedisChat = chatService.setRedisChatInfo(message, headerAccessor.getUser());

    }
}
