package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.chat.event.vo.MakeChatRoomEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendSuggestMessageEvent;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.vo.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestEventService {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 과외 신청 시 채팅방 생성 메서드
     *
     * @param authInfo 사용자 인증 정보
     * @param lessonId 과외 식별자
     * @author mozzi327
     */
    public void sendMakeChatRoom(AuthInfo authInfo, Long lessonId) {
        eventPublisher.publishEvent(MakeChatRoomEvent.builder()
                .authInfo(authInfo)
                .lessonId(lessonId)
                .build());
    }

    /**
     * 알람 메시지 전송 메서드
     *
     * @param memberId        사용자 식별자
     * @param displayName     사용자 닉네임
     * @param attendanceCount 출석일수
     * @param message         거절 사유
     * @param alarmType       알람 타입
     * @author mozzi327
     */
    public void sendAlarmMessage(Long memberId, String lessonTitle, String displayName, Integer attendanceCount, String message, AlarmType alarmType) {
        // 채팅방 메시지 최신화
        eventPublisher.publishEvent(SendSuggestMessageEvent.builder()
                .memberId(memberId)
                .lessonTitle(lessonTitle)
                .displayName(displayName)
                .attendanceCount(attendanceCount)
                .message(message)
                .alarmType(alarmType)
                .build());
    }
}
