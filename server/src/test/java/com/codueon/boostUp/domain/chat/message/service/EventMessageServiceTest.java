package com.codueon.boostUp.domain.chat.message.service;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import org.junit.jupiter.api.*;
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
    void beforeAll() {
        chatMessages = new LinkedBlockingDeque<>(99);
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());
        stompClient = webSocketTestUtils.makeStompClient();
    }

    @Test
    @DisplayName("전송된 메시지는 정상적으로 전송되어야 한다.")
    void sendMessageTest() throws Exception {
        // given
        stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
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
        stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
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
        stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        stompSession.subscribe(
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
}
