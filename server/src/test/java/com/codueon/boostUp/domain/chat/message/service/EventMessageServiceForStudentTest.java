package com.codueon.boostUp.domain.chat.message.service;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.chat.utils.DataForChat;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventMessageServiceForStudentTest extends ChatTest {
    @LocalServerPort
    protected int port;
    protected String url;
    protected String tutorToken;
    protected String studentToken;
    @Autowired
    protected EventMessageService eventMessageService;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @Autowired
    protected RedisChatRoom redisChatRoom;
    @Autowired
    protected RedisChatAlarm redisChatAlarm;
    @MockBean
    protected ChatRoomService chatRoomService;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<RedisChat> chatMessages;
    protected WebSocketStompClient stompClient;
    protected StompHeaders stompHeaders;
    protected StompSession stompSession;

    @AfterEach
    void afterEach() {
        redisChatMessage.deleteAllNewChat();
        redisChatMessage.deleteAllMessageInChatRoom(TUTOR_CHAT_ROOM_ID);
        redisChatMessage.deleteAllMessageInChatRoom(STUDENT_CHAT_ROOM_ID);
        redisChatRoom.deleteAllChatRoomKey(TUTOR_ID);
        redisChatRoom.deleteAllChatRoomKey(STUDENT_ID);
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, TUTOR_CHAT_ROOM_ID);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, STUDENT_CHAT_ROOM_ID);
        chatMessages.clear();
    }

    @BeforeAll
    void beforeAll() throws Exception {
        chatMessages = new LinkedBlockingDeque<>(99);
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());
        stompClient = webSocketTestUtils.makeStompClient();

        stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );
    }

    @Test
    @DisplayName("과외 수락 알람 메시지 테스트")
    void sendAlarmChannelAcceptSuggestMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, null, AlarmType.ACCEPT
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(ACCEPT_SUGGEST_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(ACCEPT_SUGGEST_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(ACCEPT_SUGGEST_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(ACCEPT_SUGGEST_CHAT.getMessageType());
    }

    @Test
    @DisplayName("과외 거절 알람 메시지 테스트")
    void sendAlarmChannelRejectSuggestMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, REJECT_MESSAGE, AlarmType.REJECT
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(REJECT_SUGGEST_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(REJECT_SUGGEST_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(REJECT_SUGGEST_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(REJECT_SUGGEST_CHAT.getMessageType());
    }

    @Test
    @DisplayName("출석 체크 알람 메시지 테스트")
    void sendAlarmChannelCheckAttendanceMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, STUDENT_NAME, 3, null, AlarmType.CHECK_ATTENDANCE
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(CHECK_ATTENDANCE_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(CHECK_ATTENDANCE_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(CHECK_ATTENDANCE_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(CHECK_ATTENDANCE_CHAT.getMessageType());
    }

    @Test
    @DisplayName("출석 체크 취소 알람 메시지 테스트")
    void sendAlarmChannelCancelAttendanceMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, STUDENT_NAME, 4, null, AlarmType.CANCEL_ATTENDANCE
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(CANCEL_ATTENDANCE_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(CANCEL_ATTENDANCE_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(CANCEL_ATTENDANCE_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(CANCEL_ATTENDANCE_CHAT.getMessageType());
    }

    @Test
    @DisplayName("환불 요청 수락 알람 메시지 테스트")
    void sendAlarmChannelAcceptRefundMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, null, 4, null, AlarmType.ACCEPT_REFUND
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(ACCEPT_REFUND_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(ACCEPT_REFUND_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(ACCEPT_REFUND_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(ACCEPT_REFUND_CHAT.getMessageType());
    }

    @Test
    @DisplayName("환불 요청 거절 알람 메시지 테스트")
    void sendAlarmChannelRejectRefundMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, null, 0, REJECT_MESSAGE, AlarmType.REJECT_REFUND
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(REJECT_REFUND_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(REJECT_REFUND_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(REJECT_REFUND_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(REJECT_REFUND_CHAT.getMessageType());
    }

    @Test
    @DisplayName("과외 종료 알람 메시지 테스트")
    void sendAlarmChannelEndLessonMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedStudentChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                STUDENT_ID, LESSON_TITLE, TUTOR_NAME, 0, REJECT_MESSAGE, AlarmType.END
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(STUDENT_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(END_LESSON_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(END_LESSON_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(END_LESSON_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(END_LESSON_CHAT.getMessageType());
    }
}
