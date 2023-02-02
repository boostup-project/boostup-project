package com.codueon.boostUp.domain.chat.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomIdVO {
    private Long senderId;
    private Long receiverId;

    @Builder
    public ChatRoomIdVO(Long senderId, Long receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
