package com.codueon.boostUp.domain.chat.message.repository;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRepository;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.CHAT_ROOM_ID1;
import static com.codueon.boostUp.domain.chat.utils.DataForChat.TUTOR_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class ChatRepositoryTest {

    @Autowired
    protected ChatRepository chatRepository;

    @Test
    @Transactional
    @DisplayName("RDB에 저장된 백업 채팅 메시지를 30개 가져온다.")
    void findTop30ChatByChatRoomId() throws Exception {
        // given
        LocalDateTime times = LocalDateTime.now();
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            chatMessages.add(ChatMessage.builder()
                            .chatRoomId(CHAT_ROOM_ID1)
                            .displayName("Test" + i)
                            .senderId(TUTOR_ID)
                            .message("message" + i)
                            .messageType(MessageType.TALK)
                            .createdAt(times.plusMinutes(i))
                    .build());
        }
        chatRepository.saveAll(chatMessages);

        // when
        List<RedisChat> chats = chatRepository.findTop30ChatByChatRoomId(CHAT_ROOM_ID1);

        // then
        List<ChatMessage> messages = chatRepository.findAll();
        assertThat(messages.size()).isEqualTo(31); // 총 갯수
        assertThat(chats.size()).isEqualTo(30); // 조회 갯수
    }
}
