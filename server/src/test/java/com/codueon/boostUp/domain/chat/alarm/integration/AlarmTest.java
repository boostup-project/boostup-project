package com.codueon.boostUp.domain.chat.alarm.integration;

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
public class AlarmTest extends ChatTest {
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
     * 4. 발급한 토큰을 통해 알람 Stomp Header, Stomp Client, StompSession을 생성한다. <br>
     * 5. 선생님의 세션은 자신의 알람 채널을 구독한다. (선생님 메시지 알람 확인용) <br>
     * 6. 학생이 선생님에게 문의 메시지를 전송한다. <br>
     * <br>
     * <li><b>테스트 검증</b></li>
     * 7. 선샌님의 구독된 BlockingQueue(GetChatRoom)에 채팅방 알람이 정상적으로 들어왔는지 확인한다.
     */
    @Test
    @DisplayName("알림 전송 통합 테스트")
    void alarmIntegrationTest() throws Exception {
        // 1. Save Info
        Member tutor = memberRepository.save(DataForTest.getTutor());
        Member student = memberRepository.save(DataForTest.getStudent());
        Lesson savedLesson = lessonRepository.save(DataForTest.getSaveLesson());

        // 2. Generate access token
        tutorToken = jwtTokenUtils.generateAccessToken(tutor);
        studentToken = jwtTokenUtils.generateAccessToken(student);

        // 3. Create chat room
        AuthInfo authInfo = AuthInfo.builder()
                .memberId(student.getId())
                .name(student.getName())
                .build();

        Long lessonId = savedLesson.getId();
        chatRoomService.createChatRoom(authInfo, lessonId);

        // 4. Set connection for tutor and tutor
        StompHeaders studentHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient studentStompClient = webSocketTestUtils.makeStompClient();
        StompSession studentSession = webSocketTestUtils
                .getSessionAfterConnect(studentStompClient, url, new WebSocketHttpHeaders(), studentHeaders);

        StompHeaders stompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(tutorToken);
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        StompSession stompSession = webSocketTestUtils
                .getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), stompHeaders);

        // 5. Tutor alarm subscribe
        stompSession.subscribe(
                String.format("/topic/member/%d", tutor.getId()),
                new StompFrameHandlerImpl(new GetAlarmMessage(), tutorAlarm)
        );

        // 6. Send Message to Tutor
        PostMessage sendMessage = PostMessage.builder()
                .chatRoomId(CHAT_ROOM_ID1)
                .senderId(student.getId())
                .receiverId(tutor.getId())
                .messageContent("안녕하세요. 과외 문의 드립니다.")
                .build();

        studentSession.send("/app/rooms", sendMessage);

        // 7. Receive chat alarm
        GetAlarmMessage tutorChatAlarm = tutorAlarm.poll(10, TimeUnit.SECONDS);
        assertThat(tutorChatAlarm.getChatRoomId()).isEqualTo(sendMessage.getChatRoomId());
        assertThat(tutorChatAlarm.getReceiverId()).isEqualTo(sendMessage.getReceiverId());
        assertThat(tutorChatAlarm.getLatestMessage()).isEqualTo(sendMessage.getMessageContent());
        assertThat(tutorChatAlarm.getAlarmCount()).isEqualTo(2); // Enter Message + Send Message
    }
}
