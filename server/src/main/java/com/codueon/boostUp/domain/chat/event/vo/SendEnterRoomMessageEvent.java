package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendEnterRoomMessageEvent {
    private ChatRoom chatRoom;
    private RedisChat receiverChat;

    @Builder
    public SendEnterRoomMessageEvent(ChatRoom chatRoom, RedisChat receiverChat) {
        this.chatRoom = chatRoom;
        this.receiverChat = receiverChat;
    }
}
