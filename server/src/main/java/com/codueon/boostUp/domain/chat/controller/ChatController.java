package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.vo.AuthVO;
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

    /**
     * 메시지 전송 컨트롤러 메서드
     * @param message 전송할 메시지 정보
     * @param headerAccessor StompHeaderAccessor
     * @author mozzi327
     */
    @MessageMapping("/rooms")
    public void sendMessage(PostMessage message, StompHeaderAccessor headerAccessor) {
        log.info("[SEND] start {}", headerAccessor.getSessionId());
        chatService.setRedisChatInfo(message, AuthVO.of(headerAccessor.getUser()));
        log.info("[SEND] complete {}", headerAccessor.getSessionId());
    }
}
