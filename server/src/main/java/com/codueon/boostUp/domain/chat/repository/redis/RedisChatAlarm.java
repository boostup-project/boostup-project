package com.codueon.boostUp.domain.chat.repository.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisChatAlarm {
    private ValueOperations<String, Integer> operations;
    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * Redis 자료형 초기화 메서드
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    /**
     * Redis 채팅방 생성 시 알람 메시지 초기화 메서드
     * @param memberId 사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public Integer makeChatRoomAndEnterAlarm(Long receiverId, Long chatRoomId) {
        operations.set(getKey(receiverId, chatRoomId), 1);
        return operations.get(getKey(receiverId, chatRoomId));
    }

    /**
     * Redis 메시지 전송 시 알람 카운트 메서드
     * @param memberId 사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void increaseCharRoomAlarm(Long memberId, Long chatRoomId) {
        operations.increment(getKey(memberId, chatRoomId), 1);
    }

    /**
     * Redis 채팅방 알람 내역 조회 메서드
     * @param memberId 사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @return Integer(Alarm Count)
     * @author mozzi327
     */
    public Integer getAlarmCount(Long memberId, Long chatRoomId) {
        Integer count = operations.get(getKey(memberId, chatRoomId));
        return (count == null) ? 0 : count;
    }

    /**
     * Redis 채팅방 삭제 시 알람 카운트 제거 메서드
     * @param memberId 사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void deleteAlarmCount(Long memberId, Long chatRoomId) {
        redisTemplate.delete(getKey(memberId, chatRoomId));
    }

    /**
     * Redis 알람 테이블 키 생성 메서드
     * @param memberId 사용자 식별자
     * @param chatRoomId 채팅방 식별자
     * @return String(Key)
     * @author mozzi327
     */
    public String getKey(Long memberId, Long chatRoomId) {
        return "Member" + memberId + "ChatRoom" + chatRoomId + "Alarm";
    }
}
