package com.codueon.boostUp.domain.chat.alarm.service;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.utils.DataForChat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

public class ChatAlarmServiceTest extends IntegrationTest {
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected ChatRoomService chatRoomService;
    @Autowired
    protected ChatAlarmService chatAlarmService;
    @MockBean
    protected ChatRoomRepository chatRoomRepository;

    @AfterEach
    void afterEach() {
        chatService.deleteChatMessage(CHAT_ROOM_ID1);
        chatService.deleteAllNewChatMessage();
    }

    @Test
    @DisplayName("채팅방 입장 시 알람 카운트가 초기화되야 한다.")
    void enterChatRoomTest() {
        // given
        given(chatRoomRepository.findChatRoomsBySenderIdOrReceiverId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(List.of(DataForChat.getSavedChatRoom()));
        chatService.sendRedisChat(STUDENT_SEND_MESSAGE, STUDENT_INFO);
        int alarmCount = chatRoomService.findAllChatRoom(TUTOR_ID).get(0).getAlarmCount();

        // when
        chatAlarmService.initAlarm(TUTOR_ID, CHAT_ROOM_ID1);

        // then
        int afterInitAlarmCount = chatRoomService.findAllChatRoom(TUTOR_ID).get(0).getAlarmCount();
        assertThat(alarmCount).isEqualTo(1);
        assertThat(afterInitAlarmCount).isEqualTo(0);
    }
}
