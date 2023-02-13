package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSuggestMessageEvent {
    /* 과외 신청 상태에 따른 알림 전송 Event */
    /* Receiver의 채팅방 리스트를 최신화하고 알람 채팅방에 메시지 전송을 수행한다. */
    private Long memberId;
    private String lessonTitle;
    private String displayName;
    private Integer attendanceCount;
    private String message;
    private AlarmType alarmType;

    @Builder
    public SendSuggestMessageEvent(Long memberId, String lessonTitle, String displayName,
                                   Integer attendanceCount, String message, AlarmType alarmType) {
        this.memberId = memberId;
        this.lessonTitle = lessonTitle;
        this.displayName = displayName;
        this.attendanceCount = attendanceCount;
        this.message = message;
        this.alarmType = alarmType;
    }
}
