package com.codueon.boostUp.domain.chat.room.repository;

import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방이 존재할 경우 true를 반환한다.")
    void existsBySenderIdAndReceiverIdWhenExistsCaseTest() {
        // given
        chatRoomRepository.save(CHAT_ROOM1);

        // when
        boolean isExistsChatRoom = chatRoomRepository
                .existsBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(isExistsChatRoom).isTrue();
    }

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방이 존재하지 않을 경우 false를 반환한다.")
    void existsBySenderIdAndReceiverIdWhenNotExistsCaseTest() throws Exception {
        // given and when
        boolean isExistsChatRoom = chatRoomRepository
                .existsBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(isExistsChatRoom).isFalse();
    }

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방을 단건 조회한다.")
    void findBySenderIdAndReceiverIdTest() throws Exception {
        // given
        chatRoomRepository.save(CHAT_ROOM1);

        // when
        Optional<ChatRoom> findChatRoom = chatRoomRepository
                .findBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(findChatRoom.isPresent()).isTrue();
    }

    @Test
    @DisplayName("사용자 식별자에 해당하는 채팅방을 전체 조회한다.")
    void findChatRoomsBySenderIdOrReceiverIdTest() throws Exception {
        // given
        List<ChatRoom> chatRooms = List.of(CHAT_ROOM1, CHAT_ROOM2);
        chatRoomRepository.saveAll(chatRooms);

        // when
        List<ChatRoom> findChatRooms = chatRoomRepository
                .findChatRoomsBySenderIdOrReceiverId(STUDENT_ID, STUDENT_ID);

        // then
        assertThat(findChatRooms.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자 식별자에 대한 채팅방을 전부 삭제한다.")
    void deleteAllBySenderIdOrReceiverIdTest() throws Exception {
        // given
        List<ChatRoom> chatRooms = List.of(CHAT_ROOM1, CHAT_ROOM2);
        chatRoomRepository.saveAll(chatRooms);

        // when
        chatRoomRepository.deleteAllBySenderIdOrReceiverId(STUDENT_ID, STUDENT_ID);

        // then
        List<ChatRoom> findChatRooms = chatRoomRepository
                .findChatRoomsBySenderIdOrReceiverId(STUDENT_ID, STUDENT_ID);
        assertThat(findChatRooms.size()).isEqualTo(0);
    }
}
