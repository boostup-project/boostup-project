package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @MessageMapping("/rooms")
    public void sendMessage(PostMessage message, Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = 1L;
        log.info("[SEND] start {}", memberId);
        RedisChat convertedRedisChat = chatService.setRedisChatInfo(message, memberId);
        redisTemplate.convertAndSend(channelTopic.getTopic(), convertedRedisChat);
        log.info("[SEND] complete {}", memberId);
    }
}
