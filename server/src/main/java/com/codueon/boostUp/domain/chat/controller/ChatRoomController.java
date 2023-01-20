package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/{room-id}/messages")
    public ResponseEntity getMessageInChatRoom(@PathVariable("room-id") Long chatRoomId,
                                               @RequestParam String sessionId) {
        List<RedisChat> response = chatService.getChatMessages(chatRoomId, sessionId);
        return ResponseEntity.ok().body(response);
    }
}
