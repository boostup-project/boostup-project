package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.PostRoom;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.redis.RedisSubscriber;
import com.codueon.boostUp.domain.chat.repository.ChatRepository;
import com.codueon.boostUp.domain.chat.repository.RoomRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final MemberDbService memberDbService;
    private final Map<String, ChannelTopic> topics;
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    /**
     * 채팅방 식별자 불러오기 및 생성 메서드
     * @param postRoom 채팅방 정보
     * @return chatRoomId
     * @author mozzi327
     */
    public Long getOrCreate(PostRoom postRoom) {
        Member tutor = memberDbService.ifExistsReturnMember(postRoom.getTutorId());
        Member student = memberDbService.ifExistsReturnMember(postRoom.getStudentId());

        ChatRoom findRoom = chatRepository.getOrCreate(postRoom.getTutorId(), postRoom.getStudentId())
                .orElseGet(() -> ChatRoom.builder()
                        .merchant(tutor)
                        .student(student)
                        .build()
                );

        ChatRoom savedRoom = roomRepository.save(findRoom);
        // Redis topic 생성
        String roomId = "room" + savedRoom.getId();
        if (!topics.containsKey(roomId)) {
            log.info("토픽 레디스에 생성");
            ChannelTopic topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
            log.info("topics = {}", topics);
            log.info("토픽 레디스에 저장");
        }

        return savedRoom.getId();
    }

    /**
     * 채팅방 DB 전체 조회 메서드
     * @param memberId 사용자 식별자
     * @return List(ChatRoom)
     * @author mozzi327
     */
    public List<ChatRoom> findAllMyRooms(Long memberId) {
        return roomRepository.findByMerchantIdOrStudentId(memberId, memberId);
    }

    /**
     * 채팅방 DB 조회 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @return ChatRoom
     * @author mozzi327
     */
    public ChatRoom ifExistsReturnChatRoom(Long chatRoomId) {
        return roomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHATROOM_NOT_FOUND));
    }

}
