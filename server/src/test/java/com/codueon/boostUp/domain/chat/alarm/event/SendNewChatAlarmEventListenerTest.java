package com.codueon.boostUp.domain.chat.alarm.event;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.event.listener.SendNewChatAlarmEventListener;
import com.codueon.boostUp.domain.chat.event.vo.AlarmChatListEvent;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomListEvent;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
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
public class SendNewChatAlarmEventListenerTest extends IntegrationTest {
    @LocalServerPort
    protected int port;
    protected String url;
    protected String tutorToken;
    @Autowired
    protected SendNewChatAlarmEventListener eventListener;
    @Autowired
    protected RedisChatAlarm redisChatAlarm;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    protected BlockingQueue<GetChatRoom> tutorAlarm;
    protected WebSocketTestUtils webSocketTestUtils;

    @BeforeAll
    void beforeAll() {
        url = "ws://localhost:" + port + "/ws/chat";
        tutorAlarm = new LinkedBlockingDeque<>(99);
        webSocketTestUtils = new WebSocketTestUtils();
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
    }

    @AfterEach
    void afterEach() {
        redisChatMessage.deleteAllMessageInChatRoom(TUTOR_CHAT_ROOM_ID);
        redisChatMessage.deleteAllMessageInChatRoom(STUDENT_CHAT_ROOM_ID);
        redisChatMessage.deleteAllNewChat();
        redisChatAlarm.deleteAlarmCount(TUTOR_ID, CHAT_ROOM_ID1);
        redisChatAlarm.deleteAlarmCount(STUDENT_ID, CHAT_ROOM_ID1);
    }

    @Test
    @DisplayName("Sender 알람 메시지 전송 이벤트 테스트")
    void handleSenderChatRoomAlarmTest() throws Exception {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        AlarmChatListEvent event = AlarmChatListEvent.builder()
                .chatRoomId(CHAT_ROOM_ID1)
                .memberId(STUDENT_ID)
                .isReceiver(false)
                .build();

        // when
        eventListener.handleChatRoomAlarm(event);

        // then
        int senderAlarmCount = redisChatAlarm.getAlarmCount(STUDENT_ID, CHAT_ROOM_ID1);
        assertThat(senderAlarmCount).isEqualTo(0);
    }

    @Test
    @DisplayName("Receiver 알람 메시지 전송 이벤트 테스트")
    void handleReceiverChatRoomAlarmTest() throws Exception {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        AlarmChatListEvent event = AlarmChatListEvent.builder()
                .chatRoomId(CHAT_ROOM_ID1)
                .memberId(TUTOR_ID)
                .isReceiver(true)
                .build();

        // when
        eventListener.handleChatRoomAlarm(event);

        // then
        int senderAlarmCount = redisChatAlarm.getAlarmCount(TUTOR_ID, CHAT_ROOM_ID1);
        assertThat(senderAlarmCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Receiver에게 최초 입장 알람 메시지 전송 이벤트 테스트")
    void handleEnterChatRoomAlarmTest() throws Exception {
        // given
        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        tutorSession.subscribe(
                String.format("/topic/member/%d", TUTOR_ID),
                new StompFrameHandlerImpl(new GetChatRoom(), tutorAlarm)
        );

        InitialChatRoomListEvent event = InitialChatRoomListEvent.builder()
                .chatRoom(DataForChat.getSavedChatRoom())
                .receiverChat(AlarmMessageUtils.makeMemberAlarmMessage(
                        CHAT_ROOM_ID1,
                        TUTOR_ID,
                        null,
                        TUTOR_NAME,
                        0,
                        null,
                        AlarmType.ENTER))
                .build();

        // when
        eventListener.handleEnterChatRoomAlarm(event);

        // then
        GetChatRoom chatRoom = tutorAlarm.poll(10, TimeUnit.SECONDS);
        assertThat(chatRoom.getChatRoomId()).isEqualTo(CHAT_ROOM_ID1);
        assertThat(chatRoom.getLatestMessage()).isEqualTo("[알림] " + TUTOR_NAME + "님이 입장하셨습니다.");
        assertThat(chatRoom.getAlarmCount()).isEqualTo(1);
        assertThat(chatRoom.getDisplayName()).isEqualTo(STUDENT_NAME);
    }
}
