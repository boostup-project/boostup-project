package com.codueon.boostUp.domain.chat.room.redis;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RedisChatRoomTest extends IntegrationTest {
    @Autowired
    protected RedisChatRoom redisChatRoom;

    @AfterEach
    void afterEach() {
        redisChatRoom.deleteAllChatRoomKey(TUTOR_ID);
        redisChatRoom.deleteAllChatRoomKey(STUDENT_ID);
    }

    @Test
    @DisplayName("채팅방 식별자와 사용자 식별자에 해당하는 식별 키를 저장한다.")
    void createChatRoomTest() {
        // given and when
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);

        // then
        assertThat(redisChatRoom.isExistMemberInChatRoom(CHAT_ROOM_ID1, TUTOR_ID)).isTrue();
    }

    @Test
    @DisplayName("채팅방 식별 키가 존재하지 않을 경우 키를 생성하여 저장한다.")
    void isNotExistMemberInChatRoomMakeRoomInfoTest() {
        // given and when
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(CHAT_ROOM_ID1, TUTOR_ID);

        // then
        assertThat(redisChatRoom.isExistMemberInChatRoom(CHAT_ROOM_ID1, TUTOR_ID)).isTrue();
    }

    @Test
    @DisplayName("채팅방 식별 키가 존재할 경우 true를 반환한다.")
    void isExistMemberInChatRoomTest() {
        // given and when
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);

        // then
        assertThat(redisChatRoom.isExistMemberInChatRoom(CHAT_ROOM_ID1, TUTOR_ID)).isTrue();
    }

    @Test
    @DisplayName("채팅방 식별 키가 존재하지 않을 경우 false를 반환한다.")
    void isNotExistMemberInChatRoomTest() {
        // given and when and then
        assertThat(redisChatRoom.isExistMemberInChatRoom(CHAT_ROOM_ID1, TUTOR_ID)).isFalse();
    }

    @Test
    @DisplayName("String 형태의 채팅 방 식별 키를 전체 조회한다.")
    void findAllChatRoomTest() {
        // given
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);
        redisChatRoom.createChatRoom(CHAT_ROOM_ID2, TUTOR_ID);

        // when
        List<String> keys = redisChatRoom.findAllChatRoom(TUTOR_ID);

        // then
        assertThat(keys.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Long 형태의 채팅 방 식별 키를 전체 조회한다.")
    void findAllChatRoomAsLongTest() {
        // given
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);
        redisChatRoom.createChatRoom(CHAT_ROOM_ID2, TUTOR_ID);

        // when
        List<Long> keys = redisChatRoom.findAllChatRoomAsLong(TUTOR_ID);

        // then
        assertThat(keys.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("채팅방을 나갈 시 해당 채팅방 식별 키를 삭제한다.")
    void deleteChatRoomKeyTest() {
        // given
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);
        redisChatRoom.createChatRoom(CHAT_ROOM_ID2, TUTOR_ID);

        // when
        redisChatRoom.deleteChatRoomKey(CHAT_ROOM_ID1, TUTOR_ID);

        // then
        assertThat(redisChatRoom.isExistMemberInChatRoom(CHAT_ROOM_ID1, TUTOR_ID)).isFalse();
    }

    @Test
    @DisplayName("회원 탈퇴 시 관련 채팅방을 전체 삭제한다.")
    void deleteAllChatRoomKeyTest() {
        // given
        redisChatRoom.createChatRoom(CHAT_ROOM_ID1, TUTOR_ID);
        redisChatRoom.createChatRoom(CHAT_ROOM_ID2, TUTOR_ID);

        // when
        redisChatRoom.deleteAllChatRoomKey(TUTOR_ID);

        // then
        assertThat(redisChatRoom.findAllChatRoom(TUTOR_ID).size()).isEqualTo(0);
    }
}
