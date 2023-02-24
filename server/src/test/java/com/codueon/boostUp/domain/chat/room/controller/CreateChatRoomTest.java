package com.codueon.boostUp.domain.chat.room.controller;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.ChatRoomTest;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateChatRoomTest extends ChatRoomTest {

    @LocalServerPort
    protected int port;
    @Autowired
    protected LessonRepository lessonRepository;
    protected String url;
    protected WebSocketTestUtils webSocketTestUtils;
    protected BlockingQueue<RedisChat> chatRoomMessages;

    @BeforeEach
    void setUp() {
        webSocketTestUtils = new WebSocketTestUtils();
        url = "ws://localhost:" + port + "/ws/chat";
        chatRoomMessages = new LinkedBlockingDeque<>(99);
    }

    @Test
    @DisplayName("채팅방 생성 통합 테스트")
    void createChatRoomTest() throws Exception {
        studentToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedTutor());
        tutorToken = jwtTokenUtils.generateAccessToken(DataForTest.getSavedStudent());

        // 1. Save Info
        memberRepository.saveAll(List.of(DataForTest.getTutor(), DataForTest.getStudent()));
        lessonRepository.save(DataForTest.getSaveLesson());

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

        AuthVO authInfo = AuthVO.builder()
                .memberId(2L)
                .name("학생이에요")
                .build();

        // 4. Create ChatRoom And Receive ChatRoom Message
        Long lessonId = 1L;
        chatRoomService.createChatRoom(authInfo, lessonId);
        RedisChat firstEnterChat = chatRoomMessages.poll(5, TimeUnit.SECONDS);
        RedisChat secondEnterChat = chatRoomMessages.poll(10, TimeUnit.SECONDS);

        assertThat(firstEnterChat.getMessage())
                .isEqualTo("[알림] 학생이에요님이 입장하셨습니다.");
        assertThat(secondEnterChat.getMessage())
                .isEqualTo("[알림] 선생이에요님이 입장하셨습니다.");
    }
}
