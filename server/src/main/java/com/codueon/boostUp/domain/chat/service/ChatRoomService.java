package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomListEvent;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomMessageEvent;
import com.codueon.boostUp.domain.chat.repository.querydsl.ChatRoomRepository;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.vo.AuthInfo;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisChatRoom redisChatRoom;
    private final RedisChatAlarm redisChatAlarm;
    private final MemberDbService memberDbService;
    private final RedisChatMessage redisChatMessage;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원가입 시 발생되는 MemberAlarm 생성 및 전송 이벤트 처리 메서드
     *
     * @param memberId       사용자 식별자
     * @param memberNickname 사용자 닉네임
     * @author mozzi327
     */
    @Transactional
    public void createAlarmChatRoom(Long memberId, String memberNickname) {
        if (isExistRoom(memberId, memberId))
            throw new BusinessLogicException(ExceptionCode.CHATROOM_ALREADY_EXIST);
        String CODUEON_MANAGER = "코듀온 알리미";
        ChatRoom savedChatRoom = makeChatRoomThenReturn(memberId, memberId, CODUEON_MANAGER, CODUEON_MANAGER);
        Long chatRoomId = savedChatRoom.getId();
        redisChatRoom.createChatRoom(chatRoomId, memberId);
        RedisChat alarmChat = AlarmMessageUtils
                .makeMemberAlarmMessage(chatRoomId, memberId, null, memberNickname, null, null, AlarmType.JOIN);
        sendMakeRoomMessage(chatRoomId, alarmChat, 0);
    }

    /**
     * 채팅방 생성 메서드
     *
     * @param authInfo 사용자 인증 정보
     * @param lessonId 과외 식별자
     * @author mozzi327
     */
    @Transactional
    public void createChatRoom(AuthInfo authInfo, Long lessonId) {
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
    public void saveChatRoomAndSendEnterMessage(Long senderId, Long receiverId, String senderName,
                                                String receiverName) {
        ChatRoom savedChatRoom = makeChatRoomThenReturn(senderId, receiverId, senderName, receiverName);
        Long chatRoomId = savedChatRoom.getId();

        RedisChat senderChat = AlarmMessageUtils.makeMemberAlarmMessage(chatRoomId, senderId, null, senderName, null, null, AlarmType.ENTER);
        RedisChat receiverChat = AlarmMessageUtils.makeMemberAlarmMessage(chatRoomId, receiverId, null, receiverName, null, null, AlarmType.ENTER);

        checkExistRoomKeyInfo(chatRoomId, senderId, receiverId);

        sendMakeRoomMessage(chatRoomId, senderChat, 0);
        sendMakeRoomMessage(chatRoomId, receiverChat, 1);

        eventPublisher.publishEvent(InitialChatRoomListEvent.builder()
                .chatRoom(savedChatRoom)
                .receiverChat(receiverChat)
                .build());
    }

    /**
     * 입장 메시지 전송 이벤트 발급 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param enterChat  입장 메시지
     * @param count      알람 카운트
     * @author mozzi327
     */
    public void sendMakeRoomMessage(Long chatRoomId, RedisChat enterChat, int count) {
        eventPublisher.publishEvent(InitialChatRoomMessageEvent.builder()
                .chatRoomId(chatRoomId)
                .enterChat(enterChat)
                .count(count)
                .build());
    }

    /**
     * 채팅방 저장 후 리턴 메서드
     *
     * @param senderId     사용자 식별자(Sender)
     * @param receiverId   사용자 식별자(Receiver)
     * @param senderName   닉네임(Sender)
     * @param receiverName 닉네임(Receiver)
     * @return ChatRoom
     * @author mozzi327
     */
    public ChatRoom makeChatRoomThenReturn(Long senderId, Long receiverId, String senderName,
                                           String receiverName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .senderName(senderName)
                .receiverName(receiverName)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 채팅방 목록 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return List(GetChatRoom)
     * @author mozzi327
     */
    public List<GetChatRoom> findAllChatRoom(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsBySenderIdOrReceiverId(memberId, memberId);
        return chatRooms.stream()
                .map(chatRoom -> {
                            RedisChat latestChat = redisChatMessage.getLatestMessage(redisChatMessage.getKey(chatRoom.getId()));
                            return GetChatRoom.builder()
                                    .chatRoomId(chatRoom.getId())
                                    .receiverId(chatRoom.returnReceiverId(memberId))
                                    .displayName(chatRoom.returnChatRoomName(memberId))
                                    .alarmCount(redisChatAlarm.getAlarmCount(memberId, chatRoom.getId()))
                                    .latestMessage(latestChat.getMessage())
                                    .createdAt(latestChat.getCreatedAt())
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

    /**
     * 채팅방 삭제 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param memberId   사용자 식별자
     * @author mozzi327
     */
    public void leaveChatRoom(Long chatRoomId, Long memberId) {
        chatRoomRepository.deleteById(chatRoomId);
        redisChatRoom.deleteChatRoomKey(chatRoomId, memberId);
    }

    /**
     * 채팅방 전체 삭제 메서드
     *
     * @param memberId 사용자 식별자
     * @author mozzi327
     */
    public void deleteRedisChatRoomKey(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsBySenderIdOrReceiverId(memberId, memberId);
        if (chatRooms.size() != 0) chatRoomRepository.deleteAll(chatRooms);
        redisChatRoom.deleteAllChatRoomKey(memberId);
    }

    /**
     * 채팅방 식별자 전체 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return List(String)
     * @author mozzi327
     */
    public List<String> findAllChatRoomKey(Long memberId) {
        return redisChatRoom.findAllChatRoom(memberId);
    }

    /**
     * 채팅방 식별자 전체 조회 메서드 (Long)
     *
     * @param memberId 사용자 식별자
     * @return List(Long)
     * @author mozzi327
     */
    public List<Long> findAllChatRoomKeyAsLong(Long memberId) {
        return redisChatRoom.findAllChatRoomAsLong(memberId);
    }

    /**
     * Redis 채팅방 키 존재 유무 확인 메서드
     *
     * @param chatRoomId 채팅방 식별자
     * @param senderId   사용자 식별자(Sender)
     * @param receiverId 사용자 식별자(Receiver)
     * @author mozzi327
     */
    public void checkExistRoomKeyInfo(Long chatRoomId, Long senderId, Long receiverId) {
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(chatRoomId, senderId);
        redisChatRoom.isNotExistMemberInChatRoomMakeRoomInfo(chatRoomId, receiverId);
    }

    /**
     * 알람 채팅방 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return ChatRoom
     * @author mozzi327
     */
    public ChatRoom ifExistsAlarmChatRoomThenReturn(Long memberId) {
        return chatRoomRepository.findBySenderIdAndReceiverId(memberId, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHATROOM_NOT_FOUND));
    }
}
