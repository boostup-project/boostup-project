package com.codueon.boostUp.domain.chat.message.event;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.listener.SendMessageEventListener;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendSuggestMessageEvent;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import com.codueon.boostUp.domain.chat.utils.*;
import com.codueon.boostUp.domain.utils.DataForTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("classpath:sql/initChatTest.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SendMessageEventListenerTest extends ChatTest {
    @Autowired
    protected SendMessageEventListener eventListener;
    @LocalServerPort
    protected int port;
    protected String url;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<RedisChat> chatRoomMessages;

    @BeforeAll
    void beforeAll() {
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        chatRoomMessages = new LinkedBlockingDeque<>(99);
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
    }

    @AfterEach
    void afterEach() throws Exception {
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
        eventListener.handleSendMessage(sendMessage);

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
        eventListener.handleSendEnterMessage(event);

        // then
        RedisChat enterChat = chatService.getChatMessages(studentId, chatRoomId).get(0);
        assertThat(enterChat.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(enterChat.getMessage()).isEqualTo("[알림] "+ studentName + "님이 입장하셨습니다.");
        assertThat(enterChat.getSenderId()).isEqualTo(studentId);
        assertThat(enterChat.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(enterChat.getMessageType()).isEqualTo(MessageType.ALARM);
    }

    @Test
    @DisplayName("과외 신청 과정에 대한 알람 메시지 이벤트 발급 시 메시지가 정상적으로 전송되어야 한다.")
    void sendAlarmMessageTest() throws Exception {
        // given
        AlarmType alarmType = AlarmType.REGISTER;

        chatRoomService.createAlarmChatRoom(TUTOR_ID, TUTOR_NAME);

        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl(new RedisChat(), chatRoomMessages)
        );

        SendSuggestMessageEvent event = SendSuggestMessageEvent.builder()
                .memberId(TUTOR_ID)
                .lessonTitle(LESSON_TITLE)
                .displayName(STUDENT_NAME)
                .attendanceCount(0)
                .message(null)
                .alarmType(alarmType)
                .build();

        // when
        eventListener.handleSendAlarmMessage(event);

        // then
        RedisChat suggestMessage = chatRoomMessages.poll(10, TimeUnit.SECONDS);

        assertThat(suggestMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(suggestMessage.getSenderId()).isEqualTo(TUTOR_ID);
        assertThat(suggestMessage.getMessage()).isEqualTo(REGISTER_SUGGEST_CHAT.getMessage());
        assertThat(suggestMessage.getDisplayName()).isEqualTo("코듀온 알리미");
        assertThat(suggestMessage.getMessageType()).isEqualTo(MessageType.ALARM);
    }
}
