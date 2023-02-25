package com.codueon.boostUp.domain.chat.room.controller;

import com.codueon.boostUp.domain.chat.ChatTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("classpath:sql/initChatTest.sql")
public class CreateChatRoomTest extends ChatTest {

    @LocalServerPort
    protected int port;
    protected String url;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<RedisChat> chatRoomMessages;

    @BeforeEach
    void setUp() {
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        chatRoomMessages = new LinkedBlockingDeque<>(99);
    }

    @AfterEach
    void afterAll() {
        chatService.deleteAllNewChatMessage();
        chatService.deleteChatMessage(1L);
        chatService.deleteChatMessage(2L);
        chatAlarmService.initAlarm(1L, 1L);
        chatAlarmService.initAlarm(2L, 1L);
        chatRoomService.deleteRedisChatRoomKey(1L);
        chatRoomService.deleteRedisChatRoomKey(2L);
    }

    @Test
    @DisplayName("채팅방 생성 통합 테스트")
    void createChatRoomTest() throws Exception {
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());

        // 1. Save Info
        Member tutor = memberRepository.save(DataForTest.getTutor());
        Member student = memberRepository.save(DataForTest.getStudent());
        Lesson savedLesson = lessonRepository.save(DataForTest.getSaveLesson());

        Long chatRoomId = 1L;

        System.out.println("studentToken : " + studentToken);

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

        AuthInfo authInfo = AuthInfo.builder()
                .memberId(student.getId())
                .name(student.getName())
                .build();

        // 4. Create ChatRoom And Receive ChatRoom Message
        Long lessonId = savedLesson.getId();
        chatRoomService.createChatRoom(authInfo, lessonId);
        RedisChat firstEnterChat = chatRoomMessages.poll(5, TimeUnit.SECONDS);
        RedisChat secondEnterChat = chatRoomMessages.poll(10, TimeUnit.SECONDS);

        assertThat(firstEnterChat.getMessage())
                .isEqualTo("[알림] 학생이에요님이 입장하셨습니다.");
        assertThat(secondEnterChat.getMessage())
                .isEqualTo("[알림] 선생이에요님이 입장하셨습니다.");
    }
}
