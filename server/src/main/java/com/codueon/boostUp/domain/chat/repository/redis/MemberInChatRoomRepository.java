package com.codueon.boostUp.domain.chat.repository.redis;

import com.codueon.boostUp.domain.chat.entity.MemberInChatRoom;
import com.codueon.boostUp.domain.chat.utils.ChatRoomUtils;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MemberInChatRoomRepository {
    private final ObjectMapper objectMapper;
    private final ChatRoomUtils chatRoomUtils;
    private final RedisTemplate <String, MemberInChatRoom> redisTemplate;
    // Hash 자료형 <ChatRoomIdKey, SessionId[웹소켓 세션 id], MemberInChatRoom>
    private HashOperations<String, String, MemberInChatRoom> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    /**
     * Redis 채팅방 입장 메서드
     * @param chatRoomId 채팅방 식별자
     * @param sessionId 세션 식별자
     * @param memberInChatRoom 채팅방 내 사용자 정보
     * @author mozzi327
     */
    public void addMember(Long chatRoomId, String sessionId, MemberInChatRoom memberInChatRoom) {
        if (getNumberOfMemberInChatRoom(chatRoomId) > 1) // 채팅방 인원 2보다 크면 예외처리
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_MEMBER);
        hashOperations.put(chatRoomUtils.makeKey(chatRoomId), sessionId, memberInChatRoom);
    }

    /**
     * Redis 채팅방 인원 확인 메서드
     * @param chatRoomId
     * @return long
     * @author mozzi327
     */
    public long getNumberOfMemberInChatRoom(Long chatRoomId) {
        return hashOperations.size(chatRoomUtils.makeKey(chatRoomId));
    }

    /**
     * 채팅방 회원 전체 조회 메서드
     * @param chatRoomId 채팅방 식별자
     * @return Set(MemberInChatRoom)
     * @author mozzi327
     */
    public List<MemberInChatRoom> findAllByChatRoomId(Long chatRoomId) {
        return objectToList(hashOperations.values(chatRoomUtils.makeKey(chatRoomId)));
    }

    /**
     * Redis 채팅방 식별자 + 세션 식별자에 해당하는 데이터 존재 유무 확인 메서드
     * @param chatRoomKey Redis 채팅방 식별자
     * @param sessionId 세션 식별자
     * @return boolean
     * @author mozzi327
     */
    public boolean contain(String chatRoomKey, String sessionId) {
        return hashOperations.hasKey(chatRoomKey, sessionId);
    }

    /**
     * 세션 식별자와 Redis 채팅방 식별자를 통한 MemberInChatRoom 반환 메서드
     * @param chatRoomKey Redis chatRoom 식별자
     * @param sessionId 세션 식별자
     * @return MemberInChatRoom
     * @author mozzi327
     */
    public MemberInChatRoom findBySessionId(String chatRoomKey, String sessionId) {
        return objectToEntity(hashOperations.get(chatRoomKey, sessionId));
    }

    /**
     * Redis hashOperations 리스트 null 확인 메서드
     * @param object Object
     * @return List(MemberInChatRoom)
     * @author mozzi327
     */
    private List<MemberInChatRoom> objectToList(Object object) {
        if (object == null) return null;
        return Arrays.asList(objectMapper.convertValue(object, MemberInChatRoom[].class));
    }

    /**
     * Redis hashOperations 엔티티 null 확인 메서드
     * @param object Object
     * @return MemberInChatRoom
     * @author mozzi327
     */
    private MemberInChatRoom objectToEntity(Object object) {
        if (object == null) return null;
        return objectMapper.convertValue(object, MemberInChatRoom.class);
    }
}
