package com.codueon.boostUp.domain.chat.event.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmChatListEvent {
    /* Receiver의 채팅방 리스트를 최신화하기 위한 Event */
    private Long chatRoomId;
    private Long memberId;
    private boolean isReceiver;

    @Builder
    public AlarmChatListEvent(Long chatRoomId,
                              Long memberId,
                              boolean isReceiver) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.isReceiver = isReceiver;
    }
}
