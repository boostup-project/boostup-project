package com.codueon.boostUp.domain.chat.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.dto.PostMessage;
import org.springframework.data.redis.core.RedisTemplate;
import com.codueon.boostUp.domain.chat.service.ChatService;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;

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
        JwtAuthenticationToken token = (JwtAuthenticationToken) headerAccessor.getUser();
        RedisChat convertedRedisChat = chatService.setRedisChatInfo(message, token);
        redisTemplate.convertAndSend(channelTopic.getTopic(), convertedRedisChat);
        redisTemplate.convertAndSend("/topic/alarm/member/" + 1L, convertedRedisChat);
        log.info("[SEND] complete {}", headerAccessor.getSessionId());
    }
}
