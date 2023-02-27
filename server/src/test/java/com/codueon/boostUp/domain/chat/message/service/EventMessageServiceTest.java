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
public class EventMessageServiceTest extends ChatTest {
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

    @BeforeEach
    void setUp() {
        webSocketTestUtils = new WebSocketTestUtils();
        chatMessages = new LinkedBlockingDeque<>(99);
    }

    @AfterEach
    void afterEach() {
        redisChatMessage.deleteAllMessageInChatRoom(TUTOR_CHAT_ROOM_ID);
        redisChatMessage.deleteAllMessageInChatRoom(STUDENT_CHAT_ROOM_ID);
        redisChatMessage.deleteAllNewChat();
        redisChatRoom.deleteAllChatRoomKey(TUTOR_ID);
        redisChatRoom.deleteAllChatRoomKey(STUDENT_ID);
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, TUTOR_CHAT_ROOM_ID);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, STUDENT_CHAT_ROOM_ID);
    }

    @BeforeAll
    void beforeAll() {
        url = "ws://localhost:" + port + "/ws/chat";
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());
    }

    @Test
    @DisplayName("전송된 메시지는 정상적으로 전송되어야 한다.")
    void sendMessageTest() throws Exception {
        // given
        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/rooms/%d", CHAT_ROOM_ID1),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        // when
        eventMessageService.sendMessage(STUDENT_CHAT);

        // then
        RedisChat message = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(message.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(message.getMessage()).isEqualTo(STUDENT_CHAT.getMessage());
        assertThat(message.getSenderId()).isEqualTo(STUDENT_CHAT.getSenderId());
        assertThat(message.getDisplayName()).isEqualTo(STUDENT_CHAT.getDisplayName());
        assertThat(message.getMessageType()).isEqualTo(STUDENT_CHAT.getMessageType());
    }

    @Test
    @DisplayName("Sender 입장 메시지 테스트")
    void senderSendEnterMessageTest() throws Exception {
        // given
        StompHeaders studentHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient studentStompClient = webSocketTestUtils.makeStompClient();
        StompSession studentSession = webSocketTestUtils
                .getSessionAfterConnect(studentStompClient, url, new WebSocketHttpHeaders(), studentHeaders);

        studentSession.subscribe(
                String.format("/topic/rooms/%d", CHAT_ROOM_ID1),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        // when
        eventMessageService.sendEnterMessage(CHAT_ROOM_ID1, STUDENT_ENTER_CHAT, 0);

        // then
        RedisChat senderEnterChat = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(senderEnterChat.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(senderEnterChat.getMessage()).isEqualTo(STUDENT_ENTER_CHAT.getMessage());
        assertThat(senderEnterChat.getSenderId()).isEqualTo(STUDENT_ENTER_CHAT.getSenderId());
        assertThat(senderEnterChat.getDisplayName()).isEqualTo(STUDENT_ENTER_CHAT.getDisplayName());
        assertThat(senderEnterChat.getMessageType()).isEqualTo(STUDENT_ENTER_CHAT.getMessageType());
    }

    @Test
    @DisplayName("Receiver 입장 메시지 테스트")
    void receiverSendEnterMessageTest() throws Exception {
        // given
        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/rooms/%d", CHAT_ROOM_ID1),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        // when
        eventMessageService.sendEnterMessage(CHAT_ROOM_ID1, TUTOR_ENTER_CHAT, 1);

        // then
        RedisChat senderEnterChat = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(senderEnterChat.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(senderEnterChat.getMessage()).isEqualTo(TUTOR_ENTER_CHAT.getMessage());
        assertThat(senderEnterChat.getSenderId()).isEqualTo(TUTOR_ENTER_CHAT.getSenderId());
        assertThat(senderEnterChat.getDisplayName()).isEqualTo(TUTOR_ENTER_CHAT.getDisplayName());
        assertThat(senderEnterChat.getMessageType()).isEqualTo(TUTOR_ENTER_CHAT.getMessageType());
    }

    @Test
    @DisplayName("과외 신청 알람 메시지 테스트")
    void sendAlarmChannelRegisterSuggestMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedTutorChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.REGISTER
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(REGISTER_SUGGEST_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(REGISTER_SUGGEST_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(REGISTER_SUGGEST_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(REGISTER_SUGGEST_CHAT.getMessageType());
    }

    @Test
    @DisplayName("과외 수락 알람 메시지 테스트")
    void sendAlarmChannelAcceptSuggestMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
    @DisplayName("과외 취소 알람 메시지 테스트")
    void sendAlarmChannelCancelSuggestMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedTutorChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.CANCEL
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(CANCEL_SUGGEST_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(CANCEL_SUGGEST_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(CANCEL_SUGGEST_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(CANCEL_SUGGEST_CHAT.getMessageType());
    }

    @Test
    @DisplayName("결제 성공 알람 메시지 테스트")
    void sendAlarmChannelPaySuccessMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedTutorChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.PAYMENT_SUCCESS
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(PAY_SUCCESS_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(PAY_SUCCESS_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(PAY_SUCCESS_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(PAY_SUCCESS_CHAT.getMessageType());
    }

    @Test
    @DisplayName("출석 체크 알람 메시지 테스트")
    void sendAlarmChannelCheckAttendanceMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
    @DisplayName("환불 요청 알람 메시지 테스트")
    void sendAlarmChannelRefundRequestMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedTutorChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, null, AlarmType.REFUND_REQUEST
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(REFUND_REQUEST_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(REFUND_REQUEST_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(REFUND_REQUEST_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(REFUND_REQUEST_CHAT.getMessageType());
    }

    @Test
    @DisplayName("환불 요청 수락 알람 메시지 테스트")
    void sendAlarmChannelAcceptRefundMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", STUDENT_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedStudentChatRoom());

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

    @Test
    @DisplayName("과외 종료 알람 메시지 테스트")
    void sendAlarmChannelCompletedReviewMessageTest() throws Exception {
        // given
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );

        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong())).willReturn(DataForChat.getSavedTutorChatRoom());

        // when
        eventMessageService.sendAlarmChannelMessage(
                TUTOR_ID, LESSON_TITLE, STUDENT_NAME, 0, REVIEW_MESSAGE, AlarmType.COMPLETED_REVIEW
        );

        // then
        RedisChat alarmMessage = chatMessages.poll(10, TimeUnit.SECONDS);
        assertThat(alarmMessage).isNotNull();
        assertThat(alarmMessage.getChatRoomId()).isEqualTo(TUTOR_CHAT_ROOM_ID);
        assertThat(alarmMessage.getMessage()).isEqualTo(COMPLETED_REVIEW_CHAT.getMessage());
        assertThat(alarmMessage.getSenderId()).isEqualTo(COMPLETED_REVIEW_CHAT.getSenderId());
        assertThat(alarmMessage.getDisplayName()).isEqualTo(COMPLETED_REVIEW_CHAT.getDisplayName());
        assertThat(alarmMessage.getMessageType()).isEqualTo(COMPLETED_REVIEW_CHAT.getMessageType());
    }
}
