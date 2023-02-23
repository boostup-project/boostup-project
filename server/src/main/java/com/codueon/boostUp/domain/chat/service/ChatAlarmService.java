package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.vo.AuthVO;
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
     * @param authInfo 인증 정보
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void initAlarm(Long memberId, Long chatRoomId) {
        redisChatAlarm.deleteAlarmCount(memberId, chatRoomId);
    }
}
