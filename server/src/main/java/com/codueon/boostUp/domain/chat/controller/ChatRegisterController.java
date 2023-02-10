package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.service.WebSocketAuthService;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRegisterController {
    private final RedisChatRoom redisChatRoom;
    private final WebSocketAuthService webSocketAuthService;

    /**
     * 구독 시 유저 방 생성/저장 메서드
     * @param sessionId 세션 식별자
     * @param chatRoomId 채팅방 식별자
     * @param principal Principal
     * @author mozzi327
     */
    public void registerUserAndSendEnterMessage(Long chatRoomId, AuthVO authInfo) {
        boolean isExistMember = redisChatRoom.isExistMemberInChatRoom(chatRoomId, authInfo.getMemberId());
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
