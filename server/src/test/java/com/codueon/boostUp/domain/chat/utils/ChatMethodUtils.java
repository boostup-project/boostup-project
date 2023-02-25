package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMethodUtils {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final ChatAlarmService chatAlarmService;

    public void deleteAllRedisAfterTest() {
        chatService.deleteAllNewChatMessage();
        chatService.deleteChatMessage(1L);
        chatService.deleteChatMessage(2L);
        chatAlarmService.initAlarm(1L, 1L);
        chatAlarmService.initAlarm(2L, 1L);
        chatRoomService.deleteRedisChatRoomKey(1L);
        chatRoomService.deleteRedisChatRoomKey(2L);
        chatRoomService.deleteAllChatRoom();
    }
}
