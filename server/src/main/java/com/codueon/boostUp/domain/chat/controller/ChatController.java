package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/rooms")
    public void sendMessage(PostMessage message, StompHeaderAccessor headerAccessor) {
        log.info("[SEND] start {}", headerAccessor.getSessionId());
        JwtAuthenticationToken token = (JwtAuthenticationToken) headerAccessor.getUser();
        chatService.setRedisChatInfo(message, token);
        log.info("[SEND] complete {}", headerAccessor.getSessionId());
    }
}
