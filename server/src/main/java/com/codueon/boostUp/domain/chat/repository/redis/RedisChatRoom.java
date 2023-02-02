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

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisChatRoom {
    private final ChatRoomUtils chatRoomUtils;
    private SetOperations<String, String> setOperations;
    private final RedisTemplate<String, String> redisTemplate;
    // Set 자료형 사용, <MemberId, ChatRoomKey>

    /**
     * Redis setOperations 초기화 메서드(서버 실행 시)
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    public void createChatRoom(Long chatRoomId, Long memberId) {
        setOperations.add(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId));
    }

    public void isNotExistMemberInChatRoomMakeRoomInfo(Long chatRoomId, Long memberId) {
        boolean isExist = isExistMemberInChatRoom(chatRoomId, memberId);
        if (isExist) return;
        createChatRoom(chatRoomId, memberId);
    }

    public boolean isExistMemberInChatRoom(Long chatRoomId, Long memberId) {
        log.info("key : {}", chatRoomUtils.makeMemberKey(memberId));
        log.info("value : {}", chatRoomUtils.makeKey(chatRoomId));
        return setOperations.isMember(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId));
    }

    public List<String> findAllChatRoom(Long memberId) {
        return new ArrayList<>(Objects.requireNonNull(setOperations.members(chatRoomUtils.makeMemberKey(memberId))));
    }

    public void deleteChatMember(Long chatRoomId, Long memberId) {
        setOperations.remove(chatRoomUtils.makeMemberKey(memberId), chatRoomUtils.makeKey(chatRoomId));
    }
}
