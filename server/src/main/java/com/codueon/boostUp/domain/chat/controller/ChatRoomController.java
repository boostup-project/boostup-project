package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.GetChatRoom;
import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 내 메시지 전체 조회 컨트롤러 메서드
     * @param chatRoomId 채팅방 식별자
     * @param authentication 인증 정보
     * @return List(RedisChat)
     * @author mozzi327
     */
    @GetMapping("/{room-id}/messages")
    public ResponseEntity getMessageInChatRoom(@PathVariable("room-id") Long chatRoomId,
                                               Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        List<RedisChat> response = chatService.getChatMessages(token, chatRoomId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 채팅방 생성 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param authentication 인증 정보
     * @author mozzi327
     */
    @GetMapping("/create/lesson/{lesson-id}")
    public ResponseEntity createChatRoom(@PathVariable("lesson-id") Long lessonId,
                                         Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        chatRoomService.createChatRoom(token, lessonId);
        return ResponseEntity.ok().build();
    }

    /**
     * 모든 채팅방 조회 컨트롤러 메서드
     * @param authentication Authentication 정보
     * @return List(GetChatRoom)
     * @author mozzi327
     */
    @GetMapping
    public ResponseEntity getAllChatRoom(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        List<GetChatRoom> response = chatRoomService.findAllChatRoom(token);
        Collections.sort(response);
        return ResponseEntity.ok().body(response);
    }
}
