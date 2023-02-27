package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatJdbcRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveChatBatchService {
    private final RedisChatMessage redisChatMessage;
    private final ChatJdbcRepository chatJdbcRepository;

    public void saveInMemoryChatMessagesToRdb() {
        try {
            List<RedisChat> redisChats = redisChatMessage.findAllNewChat();
            chatJdbcRepository.batchInsertChatMessages(redisChats);
            redisChatMessage.deleteAllNewChat();
        } catch (Exception e) {
            log.info("[BATCH ERR] BATCH FAILED!!");
        }
    }
}
