package com.codueon.boostUp.domain.chat.event;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMessageEvent {
    private Long chatRoomId;
    private RedisChat senderChat;
    private RedisChat receiverChat;

    @Builder
    public SendMessageEvent(Long chatRoomId,
                            RedisChat senderChat,
                            RedisChat receiverChat) {
        this.chatRoomId = chatRoomId;
        this.senderChat = senderChat;
        this.receiverChat = receiverChat;
    }
}
