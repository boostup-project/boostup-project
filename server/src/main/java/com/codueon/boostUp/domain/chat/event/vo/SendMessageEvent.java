package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMessageEvent {
    /* 채팅방 메시지 전송 Event */
    private Long receiverId;
    private RedisChat receiverChat;

    @Builder
    public SendMessageEvent(Long receiverId, RedisChat receiverChat) {
        this.receiverId = receiverId;
        this.receiverChat = receiverChat;
    }
}
