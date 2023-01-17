package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.PostMessage;
import com.codueon.boostUp.domain.chat.dto.PostRoom;
import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.GetLastChat;
import com.codueon.boostUp.domain.chat.entity.RedisChat;
import com.codueon.boostUp.domain.chat.redis.RedisPublisher;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatService chatService;
    private final RoomService roomService;

    /**
     * @param roomRequest
     * @return
     */
    @GetMapping("/room")
    public ResponseEntity<Long> createOrGet(@ModelAttribute PostRoom roomRequest) {
        Long roomId = roomService.getOrCreate(roomRequest);
        return ResponseEntity.ok(roomId);
    }

    /**
     * 사용자 메시지 전송 경로 컨트롤러 메서드
     *
     * @param roomId  채팅방 식별자
     * @param message 메시지 정보
     * @author mozzi327
     */
    @MessageMapping("/message/{room-id}")
    public void chat(@DestinationVariable("room-id") Long roomId, PostMessage message) {
        LocalDateTime now = LocalDateTime.now();
        redisPublisher.publishingTopic(ChannelTopic.of("room" + roomId),
                new RedisChat(roomId, message.getSenderId(),
                        message.getContent(),
                        now));
        chatService.save(roomId, message, now);
    }

    /**
     * 채팅방 메시지 전체 조회 컨트롤러 메서드
     *
     * @param roomId 채팅방 식별자
     * @return Object
     * @author mozzi327
     */
    @GetMapping("/chat/message/{room-id}")
    public ResponseEntity<?> findAllChats(@PathVariable("room-id") Long roomId) {
        GetChatRoom chatRoom = chatService.findAllChats(roomId);
        return ResponseEntity.ok().body(chatRoom);
    }

    /**
     * 가장 최근 메시지 전체 조회 컨트롤러 메서드
     *
     * @return List(GetLastChat)
     * @author mozzi327
     */
    @GetMapping("/rooms")
    public ResponseEntity findAllLatestChats() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 하드 코딩..
        List<GetLastChat> response = chatService.findAllLatestChats(memberId);
        return ResponseEntity.ok(response);
    }
}
