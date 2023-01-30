package com.codueon.boostUp.global.handler;

import com.codueon.boostUp.domain.chat.controller.ChatRegisterController;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    ChatRegisterController chatRegisterController;

    /**
     * 권한 인증 및 방 생성을 위한 핸들러 필터 메서드
     * @param message 메시지 정보
     * @param channel 채널 정보
     * @return Message
     * @author mozzi327
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", sessionId);
            chatRegisterController.authenticate(accessor);
            log.info("[CONNECT] end {}", sessionId);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", sessionId);
            Long chatRoomId = parseRoomIdFromHeader(accessor);
            chatRegisterController.registerUserAndSendEnterMessage(sessionId, chatRoomId, accessor.getUser());
            log.info("[SUBSCRIBE] end {}", sessionId);
        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            log.info("[UNSUBSCRIBE] start {}", sessionId);
            chatRegisterController.unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[UNSUBSCRIBE] end {}", sessionId);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[DISCONNECT] start {}", sessionId);
            chatRegisterController.unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[DISCONNECT] end {}", sessionId);
        }
        return message;
    }

    /**
     * Destination 채팅방 식별자 파싱 메서드
     * @param accessor StompHeaderAccessor
     * @return Long
     * @author mozzi327
     */
    private Long parseRoomIdFromHeader(StompHeaderAccessor accessor) {
        try {
            String destination = accessor.getDestination();
            String[] parseDestination = destination.split("/");
            if (!parseDestination[parseDestination.length - 2].equals("rooms")) return null;
            return Long.parseLong(parseDestination[parseDestination.length - 1]);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_DESTINATION);
        }
    }
}
