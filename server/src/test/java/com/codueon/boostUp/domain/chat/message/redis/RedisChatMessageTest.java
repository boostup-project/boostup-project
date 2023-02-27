package com.codueon.boostUp.domain.chat.message.redis;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RedisChatMessageTest extends IntegrationTest {
    @Autowired
    protected RedisChatMessage redisChatMessage;

    @BeforeEach
    void setUp() {
        redisChatMessage.deleteAllMessageInChatRoom(TUTOR_CHAT_ROOM_ID);
        redisChatMessage.deleteAllMessageInChatRoom(STUDENT_CHAT_ROOM_ID);
        redisChatMessage.deleteAllNewChat();
    }

    @Test
    @DisplayName("최초 입장 시 순차적으로 메시지를 생성한다.")
    void initialMessage() {
        // given and when
        redisChatMessage.initialMessage(CHAT_ROOM_ID1, STUDENT_ENTER_CHAT, 0);
        redisChatMessage.initialMessage(CHAT_ROOM_ID1, TUTOR_ENTER_CHAT, 1);

        // then
        List<RedisChat> redisChats = redisChatMessage.findAll(CHAT_ROOM_ID1);
        assertThat(redisChats.size()).isEqualTo(2);
        assertThat(redisChats.get(0).getSenderId()).isEqualTo(TUTOR_ID);
        assertThat(redisChatMessage.findAllNewChat().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메시지를 Redis에 저장한다.")
    void saveChatMessageTest() {
        // given and when
        redisChatMessage.saveChatMessage(STUDENT_CHAT);

        // then
        assertThat(redisChatMessage.findAll(CHAT_ROOM_ID1).size()).isEqualTo(1);
        assertThat(redisChatMessage.findAllNewChat().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Rdb 백업 메시지를 Redis에 저장한다. 단, newChat에 저장되지 않아야 한다.")
    void saveChatMessageFromRdbTest() {
        // given and when
        redisChatMessage.saveChatMessageFromRdb(STUDENT_CHAT);

        // then
        assertThat(redisChatMessage.findAll(CHAT_ROOM_ID1).size()).isEqualTo(1);
        assertThat(redisChatMessage.findAllNewChat().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방 내의 가장 최근 메시지를 조회한다.")
    void getLatestMessageTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        RedisChat latestMessage = redisChatMessage.getLatestMessage(redisChatMessage.getKey(CHAT_ROOM_ID1));

        // then
        assertThat(latestMessage.getSenderId()).isEqualTo(TUTOR_ID);
        assertThat(latestMessage.getMessage()).isEqualTo(TUTOR_MESSAGE);
    }

    @Test
    @DisplayName("채팅방 내의 메시지를 전체 조회한다. 단, score를 기준으로 내림차순 정렬된다. (최신 순)")
    void findAllTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        List<RedisChat> findChats = redisChatMessage.findAll(CHAT_ROOM_ID1);

        // then
        assertThat(findChats.size()).isEqualTo(2);
        assertThat(findChats.get(0).getSenderId()).isEqualTo(TUTOR_ID);
        assertThat(findChats.get(0).getMessage()).isEqualTo(TUTOR_MESSAGE);
    }

    @Test
    @DisplayName("RDB 저장용 채팅 메시지를 전체 조회한다.")
    void findAllNewChatTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        List<RedisChat> findChats = redisChatMessage.findAllNewChat();

        // then
        assertThat(findChats.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방에 존재하는 모든 메시지를 삭제한다.")
    void deleteAllMessageInChatRoomTest() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        redisChatMessage.deleteAllMessageInChatRoom(CHAT_ROOM_ID1);

        // then
        assertThat(redisChatMessage.findAll(CHAT_ROOM_ID1).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅방 메시지 식별 키를 생성한다.")
    void getKeyTest() {
        // given and when and then
        assertThat(redisChatMessage.getKey(CHAT_ROOM_ID1))
                .isEqualTo("ChatRoom" + CHAT_ROOM_ID1 + "Message");
    }

    @Test
    @DisplayName("Rdb 저장용 메시지를 카운트한다.")
    void numOfNewChat() {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        Long newChatCount = redisChatMessage.numOfNewChat();

        // then
        assertThat(newChatCount).isEqualTo(2);
    }
}
