package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.chat.event.vo.MakeAlarmChatRoomEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventService {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원가입 시 AlarmChatRoom 생성 이벤트 발행 메서드
     *
     * @param memberId    사용자 식별자
     * @param displayName 사용자 닉네임
     * @author mozzi327
     */
    public void createAlarmChatRoom(Long memberId, String displayName) {
        eventPublisher.publishEvent(MakeAlarmChatRoomEvent.builder()
                .memberId(memberId)
                .memberNickname(displayName)
                .build());
    }
}
