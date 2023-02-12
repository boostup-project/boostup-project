package com.codueon.boostUp.domain.reveiw.service;

import com.codueon.boostUp.domain.chat.event.vo.SendSuggestMessageEvent;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEventService {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 알람 메시지 전송 메서드
     *
     * @param memberId        사용자 식별자
     * @param displayName     사용자 닉네임
     * @param attendanceCount 출석일수
     * @param rejectMessage   거절 사유
     * @param alarmType       알람 타입
     * @author mozzi327
     */
    public void sendAlarmMessage(Long memberId, String lessonTitle, String displayName, Integer attendanceCount, Review review, AlarmType alarmType) {
        String  message = "평점: " + review.getScore() + "\n"
                          + "후기: " + review.getComment();

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
