package com.codueon.boostUp.domain.chat.message.service;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatJdbcRepository;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.service.SaveChatBatchService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveChatBatchServiceTest extends IntegrationTest {
    @Autowired
    protected SaveChatBatchService saveChatBatchService;
    @Autowired
    protected RedisChatMessage redisChatMessage;
    @Autowired
    protected ChatJdbcRepository chatJdbcRepository;
    @Autowired
    protected ChatRepository chatRepository;

    @BeforeAll
    void beforeAll() {
        chatRepository.deleteAll();
    }

    @AfterAll
    @Sql("classpath:sql/initChatTest.sql")
    void afterEach() {
        redisChatMessage.deleteAllMessageInChatRoom(CHAT_ROOM_ID1);
        redisChatMessage.deleteAllNewChat();
    }

    @Test
    @DisplayName("Spring Batch Redis Rdb 저장 테스트")
    void saveInMemoryChatMessagesToRdb() throws Exception {
        // given
        redisChatMessage.saveChatMessage(STUDENT_CHAT);
        redisChatMessage.saveChatMessage(TUTOR_CHAT);

        // when
        saveChatBatchService.saveInMemoryChatMessagesToRdb();

        // then
        List<RedisChat> chatMessages = chatRepository.findTop30ChatByChatRoomId(CHAT_ROOM_ID1);
        List<RedisChat> inMemoryMessages = redisChatMessage.findAllNewChat();

        assertThat(chatMessages.size()).isEqualTo(2);
        assertThat(inMemoryMessages.size()).isEqualTo(0);
    }
}
