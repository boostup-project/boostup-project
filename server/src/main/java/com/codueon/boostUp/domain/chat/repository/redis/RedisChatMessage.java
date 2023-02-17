package com.codueon.boostUp.domain.chat.repository.redis;

import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisChatMessage {
    private final ObjectMapper objectMapper;
    private final String KEY_FOR_SAVED_TO_RDB = "NewChat";
    // SortedSet 자료구조 사용, <chatRoomId, ChatMessage>
    private ZSetOperations<String, Object> operations;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis SortedSet 초기화 메서드(서버 실행 시)
     *
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForZSet();
    }

    /**
     * Redis 최초 입장 시 메시지 생성 메서드
     *
     * @param chatRoomId      채팅방 식별자
     * @param enterMessage    입장 문구
     * @param count           카운트
     * @author mozzi327
     */
    public void initialMessage(Long chatRoomId, RedisChat enterMessage, int count) {
        String key = getKey(chatRoomId);
        if (operations.zCard(key) != 0) return;
        operations.add(key, enterMessage, count);
        long idx = operations.zCard(KEY_FOR_SAVED_TO_RDB);
        operations.add(KEY_FOR_SAVED_TO_RDB, enterMessage,idx + 1);
    }

    /**
     * Redis 채팅 메시지 저장 메서드
     *
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void saveChatMessage(RedisChat redisChat) {
        String key = getKey(redisChat.getChatRoomId());
        Long size = operations.zCard(key);
        operations.add(key, redisChat, size);
        long idx = operations.zCard(KEY_FOR_SAVED_TO_RDB);
        operations.add(KEY_FOR_SAVED_TO_RDB, redisChat,idx + 1);
    }

    /**
     * Redis 채팅 메시지 저장 메서드 (Rdb용)
     *
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void saveChatMessageFromRdb(RedisChat redisChat) {
        String key = getKey(redisChat.getChatRoomId());
        Long size = operations.zCard(key);
        operations.add(key, redisChat, size);
    }

    /**
     * Redis 가장 최근 메시지 조회 메서드
     *
     * @param key 식별 키
     * @return RedisChat
     * @author mozzi327
     */
    public RedisChat getLatestMessage(String key) {
        RedisChat redisChat = objectToEntity(operations.popMax(key).getValue());
        Long size = operations.zCard(key);
        if (redisChat.getChatRoomId() == null && size == 0)
            throw new BusinessLogicException(ExceptionCode.CHAT_NOT_FOUND);
        operations.add(key, redisChat, size);
        return redisChat;
    }

    /**
     * Redis 채팅방 메시지 전체 조회 메서드
     *
     * @param chatRoomId 사용자 식별자
     * @return List(RedisChat)
     * @author mozzi327
     */
    public List<RedisChat> findAll(Long chatRoomId) {
        Object results = operations.reverseRange(getKey(chatRoomId), 0, -1);
        return objectToList(results);
    }

    /**
     * Redis 채팅방 모든 메시지 삭제 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void removeAllMessageInChatRoom(Long chatRoomId) {
        redisTemplate.delete(getKey(chatRoomId));
    }

    /**
     * Redis 채팅메시지 식별 키 생성 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @return String
     * @author mozzi327
     */
    public String getKey(Long chatRoomId) {
        return "ChatRoom" + chatRoomId + "Message";
    }

    /**
     * Redis 조회 채팅 메시지 RedisChat 변환 메서드
     *
     * @param result 메시지(형변환 전)
     * @return List(RedisChat)
     * @author mozzi327
     */
    private RedisChat objectToEntity(Object result) {
        if (result == null) return null;
        return objectMapper.convertValue(result, RedisChat.class);
    }

    /**
     * Redis 조회 채팅 메시지 List(RedisChat) 변환 메서드
     *
     * @param results 메시지 리스트(형변환 전)
     * @return List(RedisChat)
     * @author mozzi327
     */
    private List<RedisChat> objectToList(Object results) {
        if (results == null) return null;
        return Arrays.asList(objectMapper.convertValue(results, RedisChat[].class));
    }

    public List<RedisChat> findAllChat() {
        Object getAllMessage = operations.range(KEY_FOR_SAVED_TO_RDB, 0, -1);
        return Arrays.asList(objectMapper.convertValue(getAllMessage, RedisChat[].class));
    }

    public void deleteAllNewChat() {
        redisTemplate.delete(KEY_FOR_SAVED_TO_RDB);
    }

    public Long numOfNewChat() {
        return operations.zCard(KEY_FOR_SAVED_TO_RDB);
    }
}
