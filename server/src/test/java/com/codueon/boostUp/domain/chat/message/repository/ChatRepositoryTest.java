package com.codueon.boostUp.domain.chat.message.repository;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRepository;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.CHAT_ROOM_ID1;
import static com.codueon.boostUp.domain.chat.utils.DataForChat.TUTOR_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRepositoryTest {

    @Autowired
    protected ChatRepository chatRepository;

    @Autowired
    private EntityManagerFactory emf;

    protected EntityManager em;

    @BeforeAll
    void beforeAll() {
        em = emf.createEntityManager();
    }

    @Test
    @Transactional
    @DisplayName("RDB에 저장된 백업 채팅 메시지를 30개 가져온다.")
    void findTop30ChatByChatRoomId() throws Exception {
        // given
        LocalDateTime times = LocalDateTime.now();

        em.getTransaction().begin();

        for (int i = 1; i <= 31; i++) {
            em.persist(ChatMessage.builder()
                            .chatRoomId(CHAT_ROOM_ID1)
                            .displayName("Test" + i)
                            .senderId(TUTOR_ID)
                            .message("message" + i)
                            .messageType(MessageType.TALK)
                            .createdAt(times.plusMinutes(i))
                    .build());
            em.flush();
            em.clear();
        }

        em.getTransaction().commit();

        // when
        List<RedisChat> chats = chatRepository.findTop30ChatByChatRoomId(CHAT_ROOM_ID1);

        // then
        // 총 갯수
        List<ChatMessage> messages = chatRepository.findAll();
        assertThat(messages.size()).isEqualTo(31);
        assertThat(chats.size()).isEqualTo(30);

    }
}
