package com.codueon.boostUp.domain.chat.message.service;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatServiceTest extends IntegrationTest {
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected RedisChatRoom redisChatRoom;
    @Autowired
    protected RedisChatAlarm redisChatAlarm;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @MockBean
    protected MemberDbService memberDbService;
    @MockBean
    protected ChatRepository chatRepository;

    protected Long tutorId, studentId;
    protected String tutorName, studentName;

    @BeforeAll
    void setUp() {
        tutorId = 1L;
        studentId = 2L;
        tutorName = "선생이에요";
        studentName = "학생이에요";
    }

    @AfterEach
    void afterEach() {
        redisChatMessage.deleteAllNewChat();
        redisChatMessage.deleteAllMessageInChatRoom(1L);
        redisChatAlarm.deleteAlarmCount(1L, 1L);
        redisChatAlarm.deleteAlarmCount(2L, 1L);
        redisChatRoom.deleteAllChatRoomKey(1L);
        redisChatRoom.deleteAllChatRoomKey(2L);
    }

    @Test
    @DisplayName("메시지 전송 테스트")
    void sendRedisChatTest() {
        // given and when
        chatService.sendRedisChat(STUDENT_SEND_MESSAGE, STUDENT_INFO);

        // then
        RedisChat latestMessage = redisChatMessage
                .getLatestMessage(redisChatMessage.getKey(1L));

        assertThat(latestMessage.getChatRoomId()).isEqualTo(1L);
        assertThat(latestMessage.getSenderId()).isEqualTo(studentId);
        assertThat(latestMessage.getMessage()).isEqualTo("안녕하세요");
    }

    @Test
    @DisplayName("채팅방 메시지 전체 조회 시 Redis에서 메시지를 조회한다.")
    void getChatMessagesExistRedisChatCaseTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        List<RedisChat> chatMessages = chatService.getChatMessages(studentId, 1L);

        // then
        assertThat(chatMessages.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방 메시지 전체 조회 시 Redis에 메시지가 없을 경우 RDB에서 조회한다.")
    void getChatMessagesNotExistRedisChatCaseTest() {
        // given
        given(chatRepository.findTop30ChatByChatRoomId(Mockito.anyLong()))
                .willReturn(List.of(STUDENT_CHAT, TUTOR_CHAT));

        // when
        List<RedisChat> chatMessages = chatService.getChatMessages(studentId, 1L);

        // then
        assertThat(chatMessages.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방에 있는 메시지를 전부 삭제한다.")
    void deleteChatMessageTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        chatService.deleteChatMessage(1L);

        // then
        List<RedisChat> chatMessages = redisChatMessage.findAll(1L);
        assertThat(chatMessages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("RDB 저장용 메시지를 전체 삭제한다.")
    void deleteAllNewChatMessageTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        chatService.deleteAllNewChatMessage();

        // then
        List<RedisChat> newMessages = redisChatMessage.findAllNewChat();
        assertThat(newMessages.size()).isEqualTo(0);
    }
}
