package com.codueon.boostUp.domain.chat.repository.redis;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
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
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    @Getter
    private final Long NUMBER_OF_CHATS_TO_SHOW = 30L;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final int MAX_CHAT_ROOM = 2;
    private final int TTL_IN_DAYS = 2;
    // SortedSet 자료구조 사용, <chatRoomId, ChatMessage>
    private ZSetOperations<String, Object> operations;

    /**
     * Redis SortedSet 초기화 메서드(서버 실행 시)
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForZSet();
    }

    /**
     * Redis 채팅 메시지 저장 메서드
     * @param redisChat 메시지 정보
     * @author mozzi327
     */
    public void saveChatMessage(RedisChat redisChat) {
        String key = getKey(redisChat.getChatRoomId());
        operations.add(key, redisChat, System.currentTimeMillis());
    }

    /**
     * Redis 채팅방 메시지 전체 조회 메서드
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
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void removeAllMessageInChatRoom(Long chatRoomId) {
        redisTemplate.delete(getKey(chatRoomId));
    }

    /**
     * Redis 채팅메시지 식별 키 생성 메서드
     * @param chatRoomId 채팅방 식별자
     * @return String
     * @author mozzi327
     */
    private String getKey(Long chatRoomId) {
        return "ChatRoom" + chatRoomId + "Message";
    }

    /**
     * Redis 조회 채팅 메시지 형변환 메서드
     * @param results 메시지 리스트(형변환 전)
     * @return List(RedisChat)
     * @author mozzi327
     */
    private List<RedisChat> objectToList(Object results) {
        if (results == null) return null;
        return Arrays.asList(objectMapper.convertValue(results, RedisChat[].class));
    }

    /**
     * Redis 가장 마지막 메시지 스코어 조회 메서드
     * @param chatRoomId 채팅방 식별자
     * @param lastSentScore 마지막 메시지 스코어
     * @return Double
     * @author mozzi327
     */
    public Double getScoreOfLastChatWithLimitN(Long chatRoomId, double lastSentScore) {
        String key = getKey(chatRoomId);
        Set<ZSetOperations.TypedTuple<Object>> scores = operations.reverseRangeByScoreWithScores(
                key, Double.MIN_VALUE, lastSentScore, 0L, NUMBER_OF_CHATS_TO_SHOW);
        if (scores.size() == 0) return null;
        return scores.stream().skip(scores.size() - 1).findFirst().get().getScore();
    }

    /**
     * 스코어 범위의 채팅 메시지를 가져오는 메서드
     * @param chatRoomId 채팅방 식별자
     * @param oldestScoreToSend 제일 마지막 문자 스코어
     * @param lastSentScore 제일 처음 문자 스코어
     * @return List(RedisChat)
     * @author mozzi327
     */
    public List<RedisChat> getMessagesFromRoomByScore(Long chatRoomId,
                                                      Double oldestScoreToSend,
                                                      double lastSentScore) {
        String key = getKey(chatRoomId);
        Set<Object> result = operations.reverseRangeByScore(key, oldestScoreToSend, lastSentScore);
        return objectToList(result);
    }
}
