package com.codueon.boostUp.domain.chat.room.repository;

import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRepository;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Optional;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomRepositoryTest {

    @Autowired
    protected ChatRoomRepository chatRoomRepository;

    @Autowired
    private EntityManagerFactory emf;

    protected EntityManager em;

    @BeforeAll
    void beforeAll() {
        em = emf.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        em.clear();
    }

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방이 존재할 경우 true를 반환한다.")
    void existsBySenderIdAndReceiverIdWhenExistsCaseTest() {
        // given

        em.getTransaction().begin();

        em.persist(CHAT_ROOM);
        em.flush();

        em.getTransaction().commit();

        // when
        boolean isExistsChatRoom = chatRoomRepository.existsBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(isExistsChatRoom).isTrue();
    }

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방이 존재하지 않을 경우 false를 반환한다.")
    void existsBySenderIdAndReceiverIdWhenNotExistsCaseTest() {
        // given and when
        boolean isExistsChatRoom = chatRoomRepository.existsBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(isExistsChatRoom).isFalse();
    }

    @Test
    @DisplayName("SenderId와 ReceiverId에 해당하는 채팅방을 조회한다.")
    void findBySenderIdAndReceiverIdTest() {
        // given
        em.getTransaction().begin();

        em.persist(CHAT_ROOM);
        em.flush();

        em.getTransaction().commit();

        // when
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findBySenderIdAndReceiverId(STUDENT_ID, TUTOR_ID);

        // then
        assertThat(findChatRoom.isPresent()).isTrue();
    }
}
