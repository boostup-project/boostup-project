package com.codueon.boostUp.domain.chat.room;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateChatRoomTest {

    @LocalServerPort
    protected int port;
    protected DataForTest data;
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected ChatRoomService chatRoomService;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    @Autowired
    protected MemberDbService memberService;
    @Autowired
    protected LessonDbService lessonService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected ChatAlarmService chatAlarmService;

    protected String url;
    protected Member tutor;
    protected Member student;
    protected Lesson lesson;
    protected String tutorToken;
    protected String studentToken;
    protected BlockingQueue<RedisChat> chatRoomMessages;
    protected static WebSocketTestUtils webSocketTestUtils;

    @BeforeEach
    void setUp() {
        data = new DataForTest();
        webSocketTestUtils = new WebSocketTestUtils(memberService, lessonService, chatService, chatRoomService, chatAlarmService);
        tutor = data.getTutor();
        student = data.getStudent();
        lesson = data.getSaveLesson();
        url = "ws://localhost:" + port + "/ws/chat";
        chatRoomMessages = new LinkedBlockingDeque<>(99);
        tutorToken = jwtTokenUtils.generateAccessToken(data.getSavedTutor());
        studentToken = jwtTokenUtils.generateAccessToken(data.getSavedStudent());
    }

    @AfterAll
    static void afterAll() {
        webSocketTestUtils.deleteAllRedisAfterTest();
    }

    @Test
    @DisplayName("채팅방 생성 통합 테스트")
    void createChatRoomTest() throws Exception {

        // 1. Save Info
        webSocketTestUtils.initializeChattingTest(tutor, student, lesson);

        Long chatRoomId = 1L;

        // 2. Set Connection For student
        StompHeaders studentHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(studentToken);
        WebSocketStompClient studentStompClient = webSocketTestUtils.makeStompClient();
        StompSession studentSession = webSocketTestUtils
                .getSessionAfterConnect(studentStompClient, url, new WebSocketHttpHeaders(), studentHeaders);

        // 3. ChatRoom Subscribe
        studentSession.subscribe(
                String.format("/topic/rooms/%d", chatRoomId),
                new StompFrameHandlerImpl(new RedisChat(), chatRoomMessages)
        );

        AuthVO authInfo = AuthVO.builder()
                .memberId(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .build();

        // 4. Create ChatRoom And Receive ChatRoom Message
        Long lessonId = 1L;
        webSocketTestUtils.applyChattingRoom(authInfo, lessonId);
        RedisChat firstEnterChat = chatRoomMessages.poll(5, TimeUnit.SECONDS);
        RedisChat secondEnterChat = chatRoomMessages.poll(10, TimeUnit.SECONDS);

        assertThat(firstEnterChat.getMessage())
                .isEqualTo("[알림] 학생이에요님이 입장하셨습니다.");
        assertThat(secondEnterChat.getMessage())
                .isEqualTo("[알림] 선생이에요님이 입장하셨습니다.");
    }
}
