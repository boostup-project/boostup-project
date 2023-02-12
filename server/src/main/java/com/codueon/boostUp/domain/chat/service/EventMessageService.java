package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMessageService {
    private final RedisChatMessage redisChatMessage;
    private final AlarmMessageUtils alarmMessageUtils;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 메시지 Redis 전송 메서드
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void sendMessage(RedisChat redisChat) {
        redisChatMessage.saveChatMessage(redisChat);
        redisTemplate.convertAndSend("chat", redisChat);
    }

    /**
     * 알람 채팅방 메시지 저장 및 전송 메서드
     * @param chatRoomId 채팅방 식별자
     * @param memberId 사용자 식벌자
     * @param memberNickname 사용자 닉네임
     * @param rejectMessage 거절 메시지
     * @param alarmType 알람 타입
     * @author mozzi327
     */
    public void sendAlarmMessage(Long chatRoomId, Long memberId, String lessonTitle,
                                 String displayName, Integer attendanceCount, String rejectMessage, AlarmType alarmType) {
        RedisChat alarmMessage = alarmMessageUtils.makeMemberAlarmMessage(chatRoomId, memberId, lessonTitle, displayName, attendanceCount, rejectMessage, alarmType);
        redisChatMessage.saveChatMessage(alarmMessage);
        redisTemplate.convertAndSend("chat", alarmMessage);
    }

    /**
     * 입장 메시지 Redis 전송 메서드
     *
     * @param chatRoomId   채팅방 식별자
     * @param senderChat   메시지 정보(Sender)
     * @param receiverChat 메시지 정보(Receiver)
     * @author mozzi327
     */
    public void sendEnterMessage(Long chatRoomId, RedisChat enterChat, int count) {
        redisChatMessage.initialMessage(chatRoomId, enterChat, count);
        redisTemplate.convertAndSend("chat", enterChat);
    }
}
