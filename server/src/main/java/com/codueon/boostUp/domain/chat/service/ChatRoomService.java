package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.event.SendMessageEvent;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisChatRoom redisChatRoom;
    private final MemberDbService memberDbService;
    private final RedisChatMessage redisChatMessage;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 채팅방 생성 메서드
     * @param token 사용자 인증 정보
     * @param lessonId 과외 식별자
     * @author mozzi327
     */
    public void createChatRoom(JwtAuthenticationToken token, Long lessonId) {
        Member receiver = memberDbService.ifExistsReturnMemberByLessonId(lessonId);
        Long senderId = token.getId();
        Long receiverId = receiver.getId();
        String senderName = token.getName();
        String receiverName = receiver.getName();
        if (isExistRoom(senderId, receiverId)) return;
        saveChatRoomAndSendEnterMessage(senderId, receiverId, senderName, receiverName);
    }

    /**
     * 채팅방 존재 유무 확인 메서드
     * @param firstId 첫번째 사용자 식별자
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
     * @param senderId 사용자 식별자(sender)
     * @param receiverId 사용자 식별자(receiver)
     * @param senderName 닉네임(sender)
     * @param receiverName 닉네임(receiver)
     * @author mozzi327
     */
    @Transactional
    public void saveChatRoomAndSendEnterMessage(Long senderId, Long receiverId, String senderName,
                                                String receiverName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        RedisChat senderChat = makeChat(savedChatRoom.getId(), senderId, senderName, MessageType.ENTER);
        RedisChat receiverChat = makeChat(savedChatRoom.getId(), receiverId, receiverName, MessageType.ENTER);
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(savedChatRoom.getId(), senderId);
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(savedChatRoom.getId(), receiverId);
        SendMessageEvent sendMessageEvent = SendMessageEvent.builder()
                .chatRoomId(savedChatRoom.getId())
                .senderChat(senderChat)
                .receiverChat(receiverChat)
                .build();
        eventPublisher.publishEvent(sendMessageEvent);
    }

    /**
     * 채팅방 목록 조회 메서드
     * @param memberId 사용자 식별자
     * @return List(RedisChat)
     * @author mozzi327
     */
    public List<RedisChat> findAllChatRoom(Long memberId) {
        List<String> chatRooms = redisChatRoom.findAllChatRoom(memberId);
        return redisChatMessage.findAllByRoomKey(chatRooms);
    }

    /**
     * RedisChat 생성 메서드
     * @param chatRoomId 채팅방 식별자
     * @param memberId 사용자 식별자
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
