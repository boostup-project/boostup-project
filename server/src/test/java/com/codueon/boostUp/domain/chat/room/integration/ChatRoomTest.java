package com.codueon.boostUp.domain.chat.room.integration;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
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
import static com.codueon.boostUp.domain.chat.utils.DataForChat.TUTOR_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("classpath:sql/initChatTest.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomTest extends ChatTest {

    @LocalServerPort
    protected int port;
    protected String url;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<GetChatRoom> tutorAlarm;
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
     * 2. 학생, 선생님의 액세스 토큰을 발급한다.<br>
     * 3. 발급한 토큰을 통해 학생과 선생님의 Stomp Header, Stomp Client, StompSession을 생성한다. <br>
     * 4. 학생의 세션은 채팅방을 구독한다. (입장 메시지 확인용) <br>
     * 5. 선생님의 세션은 자신의 알람 채널을 구독한다. (선생님 채팅방 생성 알람 확인용) <br>
     * 6. 학생과 선생님에 대한 채팅방을 생성한다. <br>
     * <br>
     * <li><b>테스트 검증</b></li>
     * 7. 채팅방이 정상적으로 생성되었는지 확인한다. <br>
     * 8. 학생의 구독된 BlockingQueue(RedisChat)에 입장 메시지가 정상적으로 들어왔는지 확인한다. <br>
     * 9. 선샌님의 구독된 BlockingQueue(GetChatRoom)에 채팅방 알람이 정상적으로 들어왔는지 확인한다.
     */
    @Test
    @DisplayName("채팅방 생성 통합 테스트")
    void chatRoomIntegrationTest() throws Exception {
        // 1. Save Info And Setting Parameter
        Member tutor = memberRepository.save(DataForTest.getTutor());
        Member student = memberRepository.save(DataForTest.getStudent());
        Lesson savedLesson = lessonRepository.save(DataForTest.getSaveLesson());

        // 2. Generate access token
        tutorToken = jwtTokenUtils.generateAccessToken(tutor);
        studentToken = jwtTokenUtils.generateAccessToken(student);

        // 3. Set connection for tutor and tutor
        StompHeaders tutorHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient tutorStompClient = webSocketTestUtils.makeStompClient();
        StompSession tutorSession = webSocketTestUtils
                .getSessionAfterConnect(tutorStompClient, url, new WebSocketHttpHeaders(), tutorHeaders);

        StompHeaders studentHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient studentStompClient = webSocketTestUtils.makeStompClient();
        StompSession studentSession = webSocketTestUtils
                .getSessionAfterConnect(studentStompClient, url, new WebSocketHttpHeaders(), studentHeaders);

        // 4. chat room subscribe
        studentSession.subscribe(
                String.format("/topic/rooms/%d", CHAT_ROOM_ID1),
                new StompFrameHandlerImpl(new RedisChat(), chatRoomMessages)
        );

        // 5. Tutor alarm subscribe
        tutorSession.subscribe(
                String.format("/topic/member/%d", tutor.getId()),
                new StompFrameHandlerImpl(new GetChatRoom(), tutorAlarm)
        );

        // 6. Create chat room
        AuthInfo authInfo = AuthInfo.builder()
                .memberId(student.getId())
                .name(student.getName())
                .build();

        Long lessonId = savedLesson.getId();
        chatRoomService.createChatRoom(authInfo, lessonId);

        // 7. Check if the chat room has been created
        assertThat(chatRoomService.findAllChatRoom(TUTOR_ID).size()).isEqualTo(1);

        // 8. Receive enter chat message
        RedisChat firstEnterChat = chatRoomMessages.poll(5, TimeUnit.SECONDS);
        RedisChat secondEnterChat = chatRoomMessages.poll(10, TimeUnit.SECONDS);

        assertThat(firstEnterChat.getMessage()).isEqualTo("[알림] 학생이에요님이 입장하셨습니다.");
        assertThat(secondEnterChat.getMessage()).isEqualTo("[알림] 선생이에요님이 입장하셨습니다.");

        // 9. Receive tutor chat alarm
        GetChatRoom tutorAlarmChat = tutorAlarm.poll(15, TimeUnit.SECONDS);

        assertThat(tutorAlarmChat.getChatRoomId()).isEqualTo(1L);
        assertThat(tutorAlarmChat.getLatestMessage()).isEqualTo("[알림] 선생이에요님이 입장하셨습니다.");
        assertThat(tutorAlarmChat.getAlarmCount()).isEqualTo(1);
        assertThat(tutorAlarmChat.getReceiverId()).isEqualTo(1L);
    }
}
