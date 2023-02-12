package com.codueon.boostUp.domain.chat.event.vo;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitialChatRoomListEvent {
    /* 채팅방 최초 생성 시 Receiver의 채팅방 리스트를 최신화하기 위한 Event */
    private ChatRoom chatRoom;
    private RedisChat receiverChat;

    @Builder
    public InitialChatRoomListEvent(ChatRoom chatRoom, RedisChat receiverChat) {
        this.chatRoom = chatRoom;
        this.receiverChat = receiverChat;
    }
}
