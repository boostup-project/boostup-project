package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatAlarmService {
    private final RedisChatAlarm redisChatAlarm;

    /**
     * Redis 알람을 초기화해주는 메서드
     *
     * @param memberId   사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void initAlarm(Long memberId, Long chatRoomId) {
        redisChatAlarm.deleteAlarmCount(memberId, chatRoomId);
    }
}
