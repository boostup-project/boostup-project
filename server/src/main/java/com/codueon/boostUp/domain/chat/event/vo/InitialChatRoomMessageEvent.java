package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialChatRoomMessageEvent {
    /* 채팅방 최초 생성 시 채팅방 입장 메시지를 전송하는 Event */
    private Long chatRoomId;
    private RedisChat enterChat;
    private int count;

    @Builder
    public InitialChatRoomMessageEvent(Long chatRoomId,
                                       RedisChat enterChat,
                                       int count) {
        this.chatRoomId = chatRoomId;
        this.enterChat = enterChat;
        this.count = count;
    }
}
