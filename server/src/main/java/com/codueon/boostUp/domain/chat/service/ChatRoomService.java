package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.event.vo.SendEnterMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendEnterRoomMessageEvent;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisChatRoom redisChatRoom;
    private final RedisChatAlarm redisChatAlarm;
    private final MemberDbService memberDbService;
    private final RedisChatMessage redisChatMessage;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 채팅방 생성 메서드
     *
     * @param authInfo 사용자 인증 정보
     * @param lessonId 과외 식별자
     * @author mozzi327
     */
    public void createChatRoom(AuthVO authInfo, Long lessonId) {
        Member receiver = memberDbService.ifExistsReturnMemberByLessonId(lessonId);
        Long senderId = authInfo.getMemberId();
        Long receiverId = receiver.getId();
        if (isExistRoom(senderId, receiverId)) return;
        String senderName = authInfo.getName();
        String receiverName = receiver.getName();
        saveChatRoomAndSendEnterMessage(senderId, receiverId, senderName, receiverName);
    }

    /**
     * 채팅방 존재 유무 확인 메서드
     *
     * @param firstId  첫번째 사용자 식별자
     * @param secondId 두번째 사용자 식별자
     * @return boolean
     * @author mozzi327
     */
    private boolean isExistRoom(Long firstId, Long secondId) {
        boolean findFirst = chatRoomRepository.existsBySenderIdAndReceiverId(firstId, secondId);
        boolean findSecond = chatRoomRepository.existsBySenderIdAndReceiverId(secondId, firstId);
        return findFirst || findSecond;
    }

    /**
     * 채팅방 저장 후 입장 메시지 전송 메서드
     *
     * @param senderId     사용자 식별자(sender)
     * @param receiverId   사용자 식별자(receiver)
     * @param senderName   닉네임(sender)
     * @param receiverName 닉네임(receiver)
     * @author mozzi327
     */
    @Transactional
    public void saveChatRoomAndSendEnterMessage(Long senderId, Long receiverId, String senderName,
                                                String receiverName) {
        ChatRoom savedChatRoom = makeChatRoomThenReturn(senderId, receiverId, senderName, receiverName);
        Long chatRoomId = savedChatRoom.getId();

        RedisChat senderChat = makeChat(savedChatRoom.getId(), senderId, senderName, MessageType.ALARM);
        RedisChat receiverChat = makeChat(savedChatRoom.getId(), receiverId, receiverName, MessageType.ALARM);

        checkExistRoomKeyInfo(chatRoomId, senderId, receiverId);

        eventPublisher.publishEvent(SendEnterMessageEvent.builder()
                .chatRoomId(savedChatRoom.getId())
                .senderChat(senderChat)
                .receiverChat(receiverChat)
                .build());

        eventPublisher.publishEvent(SendEnterRoomMessageEvent.builder()
                .chatRoom(savedChatRoom)
                .receiverChat(receiverChat)
                .build());
    }

    /**
     * 채팅방 저장 후 리턴 메서드
     * @param senderId 사용자 식별자(Sender)
     * @param receiverId 사용자 식별자(Receiver)
     * @param senderName 닉네임(Sender)
     * @param receiverName 닉네임(Receiver)
     * @return ChatRoom
     * @author mozzi327
     */
    public ChatRoom makeChatRoomThenReturn(Long senderId, Long receiverId, String senderName,
                                 String receiverName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .senderId(senderId)
                .senderName(senderName)
                .receiverId(receiverId)
                .receiverName(receiverName)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * Redis 채팅방 키 존재 유무 확인 메서드
     * @param chatRoomId 채팅방 식별자
     * @param senderId 사용자 식별자(Sender)
     * @param receiverId 사용자 식별자(Receiver)
     * @author mozzi327
     */
    public void checkExistRoomKeyInfo(Long chatRoomId, Long senderId, Long receiverId) {
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(chatRoomId, senderId);
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(chatRoomId, receiverId);
    }

    /**
     * 채팅방 목록 조회 메서드
     *
     * @param authInfo 인증 정보
     * @return List(GetChatRoom)
     * @author mozzi327
     */
    public List<GetChatRoom> findAllChatRoom(AuthVO authInfo) {
        Long memberId = authInfo.getMemberId();
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsBySenderIdOrReceiverId(memberId, memberId);
        return chatRooms.stream()
                .map(chatRoom -> GetChatRoom.builder()
                        .chatRoomId(chatRoom.getId())
                        .receiverId(chatRoom.returnReceiverId(memberId))
                        .displayName(chatRoom.returnChatRoomName(memberId))
                        .alarmCount(redisChatAlarm.getAlarmCount(memberId, chatRoom.getId()))
                        .redisChat(redisChatMessage.getLatestMessage(redisChatMessage.getKey(chatRoom.getId())))
                        .build()
                ).collect(Collectors.toList());
    }

    /**
     * RedisChat 생성 메서드
     *
     * @param chatRoomId  채팅방 식별자
     * @param memberId    사용자 식별자
     * @param displayName 닉네임
     * @param messageType 메시지 타입
     * @return RedisChat
     * @author mozzi327
     */
    public RedisChat makeChat(Long chatRoomId, Long memberId, String displayName,
                              MessageType messageType) {
        RedisChat makeChat = RedisChat.builder()
                .chatRoomId(chatRoomId)
                .messageType(messageType)
                .senderId(memberId)
                .displayName(displayName)
                .build();
        makeChat.settingCurrentTime();
        makeChat.addEnterMessage();
        return makeChat;
    }
}
