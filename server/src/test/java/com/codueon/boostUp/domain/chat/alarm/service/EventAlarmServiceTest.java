package com.codueon.boostUp.domain.chat.alarm.service;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.dto.GetAlarmMessage;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.service.EventAlarmService;
import com.codueon.boostUp.domain.chat.utils.*;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventAlarmServiceTest extends IntegrationTest {
    @LocalServerPort
    protected int port;
    protected String url;
    protected String tutorToken;
    protected String studentToken;
    @Autowired
    protected EventAlarmService alarmService;
    @Autowired
    protected RedisChatAlarm redisChatAlarm;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<GetChatRoom> enterMessages;
    protected BlockingQueue<GetAlarmMessage> alarmMessages;

    @BeforeEach
    void setUp() {
        webSocketTestUtils = new WebSocketTestUtils();
        alarmMessages = new LinkedBlockingDeque<>(99);
    }

    @BeforeAll
    void beforeAll() {
        url = "ws://localhost:" + port + "/ws/chat";
        enterMessages = new LinkedBlockingDeque<>(99);
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());
    }

    @AfterEach
    void afterEach() {
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, CHAT_ROOM_ID1);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, CHAT_ROOM_ID1);
        redisChatMessage.deleteAllNewChat();
        redisChatMessage.deleteAllMessageInChatRoom(CHAT_ROOM_ID1);
    }

    @Test
    @DisplayName("Sender일 경우 알람 카운트가 0이 되고, 알람이 정상적으로 세팅 및 전송되어야 한다.")
    void senderSetAndSendAlarmTest() throws Exception {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);

        StompHeaders studentHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient studentStompClient = webSocketTestUtils.makeStompClient();
        StompSession studentSession = webSocketTestUtils
                .getSessionAfterConnect(studentStompClient, url, new WebSocketHttpHeaders(), studentHeaders);

        studentSession.subscribe(
                String.format("/topic/member/%d", STUDENT_ID),
                new StompFrameHandlerImpl(new GetAlarmMessage(), alarmMessages)
        );

        // when
        alarmService.setAlarmAndSendAlarm(STUDENT_ID, CHAT_ROOM_ID1, false);

        // then
        GetAlarmMessage alarm = alarmMessages.poll(10, TimeUnit.SECONDS);

        assertThat(alarm.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(alarm.getReceiverId()).isEqualTo(STUDENT_ID);
        assertThat(alarm.getAlarmCount()).isEqualTo(0);
        assertThat(alarm.getLatestMessage()).isEqualTo(STUDENT_CHAT.getMessage());
    }

    @Test
    @DisplayName("Receiver일 경우 알람 카운트가 0이 되고, 알람이 정상적으로 세팅 및 전송되어야 한다.")
    void receiverSetAndSendAlarmTest() throws Exception {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);

        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/member/%d", TUTOR_ID),
                new StompFrameHandlerImpl(new GetAlarmMessage(), alarmMessages)
        );

        // when
        alarmService.setAlarmAndSendAlarm(TUTOR_ID, CHAT_ROOM_ID1, true);

        // then
        GetAlarmMessage alarm = alarmMessages.poll(10, TimeUnit.SECONDS);

        assertThat(alarm.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(alarm.getReceiverId()).isEqualTo(TUTOR_ID);
        assertThat(alarm.getAlarmCount()).isEqualTo(1);
        assertThat(alarm.getLatestMessage()).isEqualTo(STUDENT_CHAT.getMessage());
    }

    @Test
    @DisplayName("채팅방 생성 시 Receiver에게 채팅방 알람을 전송해야 한다.")
    void sendEnterAlarmTest() throws Exception {
        // given
        RedisChat enterChat = AlarmMessageUtils.makeMemberAlarmMessage(
                CHAT_ROOM_ID1, TUTOR_ID, LESSON_TITLE, TUTOR_NAME,
                0, null, AlarmType.ENTER
        );

        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/member/%d", TUTOR_ID),
                new StompFrameHandlerImpl(new GetChatRoom(), enterMessages)
        );

        // when
        alarmService.sendEnterAlarm(DataForChat.getSavedChatRoom(), enterChat);

        // then
        GetChatRoom enterAlarm = enterMessages.poll(10, TimeUnit.SECONDS);

        assertThat(enterAlarm.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(enterAlarm.getReceiverId()).isEqualTo(enterChat.getSenderId());
        assertThat(enterAlarm.getLatestMessage()).isEqualTo(enterChat.getMessage());
        assertThat(enterAlarm.getAlarmCount()).isEqualTo(1);
        assertThat(enterAlarm.getDisplayName()).isEqualTo(STUDENT_NAME);
    }
}
