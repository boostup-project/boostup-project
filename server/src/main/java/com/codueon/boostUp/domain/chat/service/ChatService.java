package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.req.PostMessage;
import com.codueon.boostUp.domain.chat.entity.ChatMessage;
import com.codueon.boostUp.domain.chat.entity.ChatRoom;
import com.codueon.boostUp.domain.chat.repository.ChatRepository;
import com.codueon.boostUp.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;
    private final RoomService roomService;
//    private final MemberDbService memberDbService;


    public void save(Long roomId, PostMessage message, LocalDateTime now) {
//        Member sender = memberDbService.findById(message.getSenderId());
//        Member receiver = memberDbService.findById(message.getReceiverId());
//        ChatRoom room = roomService.findById(roomId);
//        ChatMessage chat = ChatMessage.builder()
//                .sender(sender)
//                .receiver(receiver)
//                .content(message.getContent())
//                .room(room)
//                .build();
//        chatRepository.save(chat);
        // TODO : Alarm 호출 서비스 구현 요
    }
}
