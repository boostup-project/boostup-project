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
public class EventMessageServiceForTutorTest extends ChatTest {
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
        redisChatMessage.deleteAllMessageInChatRoom(TUTOR_CHAT_ROOM_ID);
        redisChatMessage.deleteAllMessageInChatRoom(STUDENT_CHAT_ROOM_ID);
        redisChatMessage.deleteAllNewChat();
        redisChatRoom.deleteAllChatRoomKey(TUTOR_ID);
        redisChatRoom.deleteAllChatRoomKey(STUDENT_ID);
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, TUTOR_CHAT_ROOM_ID);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, STUDENT_CHAT_ROOM_ID);
        chatMessages.clear();
    }

    @BeforeAll
    void beforeAll() throws Exception {
        webSocketTestUtils = new WebSocketTestUtils();
        chatMessages = new LinkedBlockingDeque<>(99);
        url = "ws://localhost:" + port + "/ws/chat";
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());
        stompClient = webSocketTestUtils.makeStompClient();
        stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
                String.format("/topic/rooms/%d", TUTOR_CHAT_ROOM_ID),
                new StompFrameHandlerImpl<>(new RedisChat(), chatMessages)
        );
    }

    @Test
    @DisplayName("과외 신청 알람 메시지 테스트")
    void sendAlarmChannelRegisterSuggestMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedTutorChatRoom());

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
    @DisplayName("과외 취소 알람 메시지 테스트")
    void sendAlarmChannelCancelSuggestMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedTutorChatRoom());

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
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedTutorChatRoom());

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
    @DisplayName("환불 요청 알람 메시지 테스트")
    void sendAlarmChannelRefundRequestMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedTutorChatRoom());

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
    @DisplayName("과외 종료 알람 메시지 테스트")
    void sendAlarmChannelCompletedReviewMessageTest() throws Exception {
        // given
        given(chatRoomService.ifExistsAlarmChatRoomThenReturn(Mockito.anyLong()))
                .willReturn(DataForChat.getSavedTutorChatRoom());

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
