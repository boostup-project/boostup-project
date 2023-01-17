package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.GetChat;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.GetLastChat;
import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.ChatRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;
    private final RoomService roomService;
    private final MemberDbService memberDbService;

    /**
     * 채팅방에 있는 모든 채팅을 가져오는 메서드
     * @param chatRoomId 채팅방 식별자
     * @return GetChatRoom
     * @author mozzi327
     */
    public GetChatRoom findAllChats(Long chatRoomId) {
        ChatRoom findChatRoom = roomService.ifExistsReturnChatRoom(chatRoomId);
        List<ChatMessage> findAllChat = chatRepository.findAllChatsInRoom(chatRoomId);

        List<GetChat> chatRes = findAllChat.stream()
                .map(GetChat::of)
                .collect(Collectors.toList());
        return GetChatRoom.builder()
                .chatResponses(chatRes)
                .memberA(findChatRoom.getStudent())
                .memberB(findChatRoom.getMerchant())
                .build();
    }

    /**
     * 사용자 채팅방의 가장 최신 메시지를 불러오는 메서드
     * @param memberId 사용자 식별자
     * @return List(GetLastChat)
     * @author mozzi327
     */
    public List<GetLastChat> findAllLatestChats(Long memberId) {
        List<ChatRoom> rooms = roomService.findAllMyRooms(memberId);
        List<ChatMessage> allLatestChats = chatRepository.findAllLatestChats(rooms);
        return allLatestChats.stream().map(
                chat -> GetLastChat.of(chat.getRoom().getPartner(memberId), chat)
        ).collect(Collectors.toList());
    }

    /**
     * 메시지 저장 메서드
     * @param chatRoomId 채팅방 식별자
     * @param message 메시지 정보
     * @param now 현재 시간
     * @author mozzi327
     */
    @Transactional
    public void save(Long chatRoomId, PostMessage message, LocalDateTime now) {
        Member sender = memberDbService.ifExistsReturnMember(message.getSenderId());
        Member receiver = memberDbService.ifExistsReturnMember(message.getReceiverId());
        ChatRoom room = roomService.ifExistsReturnChatRoom(chatRoomId);
        ChatMessage chat = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(message.getContent())
                .room(room)
                .createdAt(now)
                .build();
        chatRepository.save(chat);
        // TODO : Alarm 호출 서비스 구현 요
    }
}
