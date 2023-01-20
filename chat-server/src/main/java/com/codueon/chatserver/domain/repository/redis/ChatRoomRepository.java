package com.codueon.chatserver.domain.repository.redis;

import com.codueon.chatserver.domain.chat.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final ChatRoomUtils chatRoomUtils;
    private final RedisTemplate<String, String> redisTemplate;
    // Set 자료형 사용, <chatRoomKeys, ChatRoomId>
    private SetOperations<String, String> setOperations;

    /**
     * Redis setOperations 초기화 메서드(서버 실행 시)
     * @author mozzi327
     */
    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    /**
     * Redis 채팅방 생성 메서드
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void createChatRoom(Long chatRoomId) {
        setOperations.add(chatRoomUtils.KEY_FOR_CHAT_ROOMS, chatRoomUtils.makeKey(chatRoomId));
    }

    /**
     * Redis 채팅방 식별자 전체 조회 메서드
     * @return Set(String)
     * @author mozzi327
     */
    public Set<String> findAllChatRoomId() {
        return setOperations.members(chatRoomUtils.KEY_FOR_CHAT_ROOMS);
    }

    /**
     * Redis 채팅방 존재 유무 확인 메서드
     * @param chatRoomId 채팅방 식별자
     * @return boolean
     * @author mozzi327
     */
    public boolean isExistChatRoom(Long chatRoomId) {
        return setOperations.isMember(chatRoomUtils.KEY_FOR_CHAT_ROOMS, chatRoomUtils.makeKey(chatRoomId));
    }

    /**
     * Redis 채팅방 삭제 메서드
     * @param chatRoomId 채팅방 식별자
     * @author mozzi327
     */
    public void deleteChatRoom(Long chatRoomId) {
        setOperations.remove(chatRoomUtils.KEY_FOR_CHAT_ROOMS, chatRoomUtils.makeKey(chatRoomId));
    }

}
