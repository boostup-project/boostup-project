package com.codueon.boostUp.domain.chat.event.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendAlarmMessageEvent {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;

    @Builder
    public SendAlarmMessageEvent(Long chatRoomId,
                                 Long senderId,
                                 Long receiverId) {
        this.chatRoomId = chatRoomId;
        this.senderId =senderId;
        this.receiverId = receiverId;
    }
}
