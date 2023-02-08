package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatAlarmService {
    private final RedisChatAlarm redisChatAlarm;

    /**
     * Redis 알람을 0으로 초기화해주는 메서드
     * @param receiverId 사용자 식별자(Receiver)
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void sendAlarm(JwtAuthenticationToken token, Long chatRoomId) {
        if (token == null) throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        redisChatAlarm.deleteAlarmCount(token.getId(), chatRoomId);
    }
}
