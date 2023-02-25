package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.vo.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        chatService.setRedisChatInfo(message, AuthInfo.of(headerAccessor.getUser()));
        log.info("[SEND] complete {}", headerAccessor.getSessionId());
    }

    /**
     * 채팅방 내 메시지 전체 조회 컨트롤러 메서드
     * @param chatRoomId 채팅방 식별자
     * @param authentication 인증 정보
     * @return List(RedisChat)
     * @author mozzi327
     */
    @GetMapping("/chat/room/{room-id}/messages")
    public ResponseEntity getMessageInChatRoom(@PathVariable("room-id") Long chatRoomId,
                                               Authentication authentication) {
        List<RedisChat> response = chatService.getChatMessages(AuthInfo.ofMemberId(authentication), chatRoomId);
        return ResponseEntity.ok().body(response);
    }
}
