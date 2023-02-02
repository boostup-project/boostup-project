package com.codueon.boostUp.domain.chat.controller;

import java.security.Principal;

import com.codueon.boostUp.domain.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.domain.chat.service.WebSocketAuthService;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRegisterController {
    // **** StompHandler에서 사용할 인증 및 메시지 처리 컨트롤러 클래스 ****
    private final ChatService chatService;
    private final RedisChatRoom redisChatRoom;
    private final WebSocketAuthService webSocketAuthService;

    /**
     * 구독 시 유저 방 생성/저장 메서드
     * @param sessionId 세션 식별자
     * @param chatRoomId 채팅방 식별자
     * @param principal Principal
     * @author mozzi327
     */
    public void registerUserAndSendEnterMessage(Long chatRoomId, Principal principal) {
        if (principal == null) throw new AuthException(ExceptionCode.INVALID_ACCESS);
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        boolean isExistMember = redisChatRoom.isExistMemberInChatRoom(chatRoomId, token.getId());
        if (!isExistMember) throw new BusinessLogicException(ExceptionCode.CHATROOM_NOT_FOUND);
    }

    /**
     * Authenticate 정보 조회 메서드
     * @param accessor StompHeaderAccessor
     * @author mozzi327
     */
    public void authenticate(StompHeaderAccessor accessor) {
        webSocketAuthService.authenticate(accessor);
    }
}
