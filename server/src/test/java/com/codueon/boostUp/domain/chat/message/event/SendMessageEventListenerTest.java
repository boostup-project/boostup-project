package com.codueon.boostUp.domain.chat.message.event;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.listener.SendMessageEventListener;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendSuggestMessageEvent;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("classpath:sql/initChatTest.sql")
public class SendMessageEventListenerTest extends ChatTest {
    @Autowired
    protected EventMessageService eventMessageService;
    protected SendMessageEventListener sendMessageEventListener;

    @BeforeEach
    void setUp() throws Exception {
        sendMessageEventListener = new SendMessageEventListener(eventMessageService);
    }

    @AfterEach
    void afterAll() throws Exception {
        chatService.deleteAllNewChatMessage();
        chatService.deleteChatMessage(1L);
        chatService.deleteChatMessage(2L);
        chatAlarmService.initAlarm(1L, 1L);
        chatAlarmService.initAlarm(2L, 1L);
        chatRoomService.deleteRedisChatRoomKey(1L);
        chatRoomService.deleteRedisChatRoomKey(2L);
    }

    @Test
    @DisplayName("메시지 전송 이벤트 발급 시 해당 채팅룸에 대한 메시지가 정상적으로 저장되어야 한다.")
    void sendMessageTest() throws Exception {
        // given
        Long tutorId = 1L;
        Long studentId = 2L;
        Long chatRoomId = 1L;
        String studentName = "학생이에요";

        RedisChat sendMessage = RedisChat.builder()
                .chatRoomId(chatRoomId)
                .senderId(studentId)
                .displayName(studentName)
                .message("안녕하세요")
                .createdAt(LocalDateTime.now())
                .messageType(MessageType.TALK)
                .build();

        // when(Send To Tutor)
        sendMessageEventListener.handleSendMessage(sendMessage);

        // then
        RedisChat receivedMessage = chatService.getChatMessages(tutorId, chatRoomId).get(0);

        assertThat(receivedMessage.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(receivedMessage.getSenderId()).isEqualTo(studentId);
        assertThat(receivedMessage.getDisplayName()).isEqualTo(studentName);
        assertThat(receivedMessage.getMessage()).isEqualTo("안녕하세요");
        assertThat(receivedMessage.getMessageType()).isEqualTo(MessageType.TALK);
    }

    @Test
    @DisplayName("입장 메시지 전송 이벤트 발급 시 입장 메시지가 정상적으로 저장되어야 한다.")
    void sendEnterMessageTest() throws Exception {
        // given
        Long studentId = 2L;
        Long chatRoomId = 1L;
        String studentName = "학생이에요";

        RedisChat enterMessage = AlarmMessageUtils.makeMemberAlarmMessage(
                chatRoomId, studentId, null, studentName,
                null, null, AlarmType.ENTER);
        InitialChatRoomMessageEvent event = InitialChatRoomMessageEvent.builder()
                .chatRoomId(chatRoomId)
                .enterChat(enterMessage)
                .count(0)
                .build();

        // when
        sendMessageEventListener.handleSendEnterMessage(event);

        // then
        RedisChat enterChat = chatService.getChatMessages(studentId, chatRoomId).get(0);
        assertThat(enterChat.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(enterChat.getMessage()).isEqualTo("[알림] "+ studentName + "님이 입장하셨습니다.");
        assertThat(enterChat.getSenderId()).isEqualTo(studentId);
        assertThat(enterChat.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(enterChat.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("과외 신청 과정에 대한 알람 메시지 이벤트 발급 시 메시지가 정상적으로 저장되어야 한다.")
    void sendAlarmMessageTest() throws Exception {
        // given
        Long chatRoomId = 1L;
        Long tutorId = 1L;
        Long studentId = 2L;
        String lessonTitle = "자바 속성 강의!";
        String tutorName = "선생이에요";
        String studentName = "학생이에요";
        Integer attendanceCount = 0;
        AlarmType alarmType = AlarmType.REGISTER;

        chatRoomService.createAlarmChatRoom(tutorId, tutorName);

        SendSuggestMessageEvent event = SendSuggestMessageEvent.builder()
                .memberId(tutorId)
                .lessonTitle(lessonTitle)
                .displayName(studentName)
                .attendanceCount(attendanceCount)
                .message(null)
                .alarmType(alarmType)
                .build();

        // when
        sendMessageEventListener.handleSendAlarmMessage(event);

        // then
        RedisChat suggestMessage = chatService.getChatMessages(studentId, chatRoomId).get(1);

        assertThat(suggestMessage.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(suggestMessage.getSenderId()).isEqualTo(tutorId);
        assertThat(suggestMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(suggestMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }
}
