package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.vo.AlarmChatListEvent;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMessageService {
    private final RedisChatMessage redisChatMessage;
    private final ChatRoomService chatRoomService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 메시지 Redis 전송 메서드
     *
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void sendMessage(RedisChat redisChat) {
        redisChatMessage.saveChatMessage(redisChat);
        redisTemplate.convertAndSend("chat", redisChat);
    }

    /**
     * 입장 메시지 Redis 전송 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param enterChat  입장 메시지
     * @param count      알람 카운트
     * @author mozzi327
     */
    public void sendEnterMessage(Long chatRoomId, RedisChat enterChat, int count) {
        redisChatMessage.initialMessage(chatRoomId, enterChat, count);
        redisTemplate.convertAndSend("chat", enterChat);
    }

    /**
     * MemberAlarm 메시지 채팅방 식별자 조회 메서드
     *
     * @param memberId        사용자 식별자
     * @param displayName     사용자 닉네임
     * @param attendanceCount 출석일수
     * @param lessonTitle     과외 타이틀
     * @param message         사유
     * @param alarmType       알람 타입
     * @author mozzi327
     */
    @Transactional
    public void sendAlarmChannelMessage(Long memberId, String lessonTitle, String displayName, Integer attendanceCount, String message, AlarmType alarmType) {
        Long chatRoomId = chatRoomService.ifExistsAlarmChatRoomThenReturn(memberId).getId();
        RedisChat alarmChat = AlarmMessageUtils.makeMemberAlarmMessage(chatRoomId, memberId, lessonTitle, displayName, attendanceCount, message, alarmType);
        sendMessage(alarmChat);
        eventPublisher.publishEvent(AlarmChatListEvent.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .isReceiver(true)
                .build());
    }
}
