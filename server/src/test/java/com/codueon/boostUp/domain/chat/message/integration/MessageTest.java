package com.codueon.boostUp.domain.chat.message.integration;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.GetAlarmMessage;
import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthInfo;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.CHAT_ROOM_ID1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("classpath:sql/initChatTest.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageTest extends ChatTest {
    @LocalServerPort
    protected int port;
    protected String url;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<GetAlarmMessage> tutorAlarm;
    protected BlockingQueue<RedisChat> chatRoomMessages;

    @BeforeAll
    void beforeAll() {
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        tutorAlarm = new LinkedBlockingDeque<>(99);
        chatRoomMessages = new LinkedBlockingDeque<>(99);
    }

    @AfterAll
    void afterAll() {
        chatService.deleteAllNewChatMessage();
        chatService.deleteChatMessage(1L);
        chatService.deleteChatMessage(2L);
        chatAlarmService.initAlarm(1L, 1L);
        chatAlarmService.initAlarm(2L, 1L);
        chatRoomService.deleteRedisChatRoomKey(1L);
        chatRoomService.deleteRedisChatRoomKey(2L);
    }

    /**
     * <li><b>테스트 시나리오</b></li>
     *
     * 1. 학생, 선생님, 과외 정보를 저장한다. <br>
     * 2. 액세스 토큰을 발급한다.<br>
     * 3. 학생과 선생님에 대한 채팅방을 생성한다. <br>
     * 4. 발급한 토큰을 통해 채팅방 Stomp Header, Stomp Client, StompSession을 생성한다. <br>
     * 5. 학생의 세션은 채팅방을 구독한다. (전송 메시지 확인용) <br>
     * 6. 학생이 선생님에게 문의 메시지를 전송한다. <br>
     * <br>
     * <li><b>테스트 검증</b></li>
     * 7. 학생의 구독된 BlockingQueue(RedisChat)에 전송 메시지가 정상적으로 들어왔는지 확인한다. <br>
     */
    @Test
    @DisplayName("메시지 전송 통합 테스트")
    void messageIntegrationTest() throws Exception {
        // 1. Save Info
        Member tutor = memberRepository.save(DataForTest.getTutor());
        Member student = memberRepository.save(DataForTest.getStudent());
        Lesson savedLesson = lessonRepository.save(DataForTest.getSaveLesson());

        // 2. Generate access token
        studentToken = jwtTokenUtils.generateAccessToken(student);

        // 3. Create chat room
        AuthInfo authInfo = AuthInfo.builder()
                .memberId(student.getId())
                .name(student.getName())
                .build();

        Long lessonId = savedLesson.getId();
        chatRoomService.createChatRoom(authInfo, lessonId);

        // 4. Set connection for chatRoom
        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        // 5. Chat room subscribe
        stompSession.subscribe(
                String.format("/topic/rooms/%d", CHAT_ROOM_ID1),
                new StompFrameHandlerImpl(new RedisChat(), chatRoomMessages)
        );

        // 6. Send Message to Tutor
        PostMessage sendMessage = PostMessage.builder()
                .chatRoomId(CHAT_ROOM_ID1)
                .senderId(student.getId())
                .receiverId(tutor.getId())
                .messageContent("안녕하세요. 과외 문의 드립니다.")
                .build();

        stompSession.send("/app/rooms", sendMessage);

        // 7. Receive chat message
        RedisChat receiveChat = chatRoomMessages.poll(10, TimeUnit.SECONDS);
        assertThat(receiveChat.getChatRoomId()).isEqualTo(sendMessage.getChatRoomId());
        assertThat(receiveChat.getSenderId()).isEqualTo(sendMessage.getSenderId());
        assertThat(receiveChat.getDisplayName()).isEqualTo(student.getName());
        assertThat(receiveChat.getMessage()).isEqualTo(sendMessage.getMessageContent());
        assertThat(receiveChat.getMessageType()).isEqualTo(MessageType.TALK);
    }
}
