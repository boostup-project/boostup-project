package com.codueon.boostUp.domain.chat.repository.redis;


import com.codueon.boostUp.domain.chat.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisChatRoom {
    private final ChatRoomUtils chatRoomUtils;
    private SetOperations<String, String> setOperations;
    private final RedisTemplate<String, String> redisTemplate;
    // Set 자료형 사용, <memberId, chatRoomKey>

    /**
     * Redis setOperations 초기화 메서드(서버 실행 시)
     *
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    /**
     * Redis 채팅방 식별 키 생성 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param memberId   사용자 식별자
     * @author mozzi327
     */
    public void createChatRoom(Long chatRoomId, Long memberId) {
        setOperations.add(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId));
    }

    /**
     * Redis 채팅방 식별 키가 존재하지 않을 시 생성해주는 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param memberId   사용자 식별자
     * @author mozzi327
     */
    public void isNotExistMemberInChatRoomMakeRoomInfo(Long chatRoomId, Long memberId) {
        boolean isExist = isExistMemberInChatRoom(chatRoomId, memberId);
        if (isExist) return;
        createChatRoom(chatRoomId, memberId);
    }

    /**
     * Redis 채팅방 식별 키 존재 유무 조회 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param memberId   사용자 식별자
     * @return boolean
     * @author mozzi327
     */
    public boolean isExistMemberInChatRoom(Long chatRoomId, Long memberId) {
        return Boolean.TRUE.equals(setOperations.isMember(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId)));
    }

    /**
     * Redis 사용자 채팅방 식별 키 전체 조회 메서드(String)
     *
     * @param memberId 사용자 식별자
     * @return List(String)
     * @author mozzi327
     */
    public List<String> findAllChatRoom(Long memberId) {
        return new ArrayList<>(Objects.requireNonNull(setOperations.members(chatRoomUtils.makeMemberKey(memberId))));
    }

    /**
     * Redis 사용자 채팅방 식별 키 전체 조회 메서드(Long)
     *
     * @param memberId 사용자 식별자
     * @return List(Long)
     * @author mozzi327
     */
    public List<Long> findAllChatRoomAsLong(Long memberId) {
        return Objects.requireNonNull(setOperations.members(chatRoomUtils.makeMemberKey(memberId))).stream()
                .map(chatRoomUtils::parseChatRoomId)
                .collect(Collectors.toList());
    }

    /**
     * Redis 채팅방에 나갈 시 채팅방 식별 키 삭제 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param memberId   사용자 식별자
     * @author mozzi327
     */
    public void deleteChatRoomKey(Long chatRoomId, Long memberId) {
        setOperations.remove(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId));
    }

    /**
     * Redis 사용자에 대한 채팅방 식별 키 전체 삭제 메서드
     *
     * @param memberId 사용자 식별자
     * @author mozzi327
     */
    public void deleteAllChatRoomKey(Long memberId) {
        redisTemplate.delete(chatRoomUtils.makeMemberKey(memberId));
    }
}
