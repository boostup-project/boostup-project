package com.codueon.boostUp.domain.chat.event.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeAlarmChatRoomEvent {
    /* 회원가입 시 알림 채팅방을 생성하는 Event */
    /* 알림 채팅방 생성과 회원가입 메시지 저장을 수행한다. */
    private Long memberId;
    private String memberNickname;

    @Builder
    public MakeAlarmChatRoomEvent(Long memberId, String memberNickname) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }
}
