package com.codueon.boostUp.global.handler;

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
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        String sessionId = accessor.getSessionId();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", sessionId);
            log.info("[Access Token] accessToken {}", accessToken);
            log.info("[CONNECT] end {}", sessionId);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", sessionId);
            log.info("[SUBSCRIBE] end {}", sessionId);
        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            log.info("[UNSUBSCRIBE] start {}", sessionId);
            log.info("[UNSUBSCRIBE] end {}", sessionId);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[DISCONNECT] start {}", sessionId);
            log.info("[DISCONNECT] end {}", sessionId);
        }
        return message;
    }
}
