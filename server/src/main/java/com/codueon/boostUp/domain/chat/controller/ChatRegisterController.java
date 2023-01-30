package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.entity.MemberInChatRoom;
import com.codueon.boostUp.domain.chat.entity.MemberInfoInChatRoom;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.service.WebSocketAuthService;
import com.codueon.boostUp.domain.chat.utils.MessageType;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRegisterController {
    // **** StompHandler에서 사용할 인증 및 메시지 처리 컨트롤러 클래스 ****
    private final ChatService chatService;
    private final ChannelTopic channelTopic;
    private final ChatRoomService chatRoomService;
    private final MemberDbService memberDbService;
    private final WebSocketAuthService webSocketAuthService;
    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 구독 시 유저 방 생성/저장 메서드
     * @param sessionId 세션 식별자
     * @param chatRoomId 채팅방 식별자
     * @param Principal Principal
     * @author mozzi327
     */
    public void registerUserAndSendEnterMessage(String sessionId, Long chatRoomId, Principal principal) {
        if (chatRoomId == null && principal == null) return;
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        Member findMember = memberDbService.ifExistsReturnMember(token.getId());
        chatRoomService.saveMemberInChatRoom(chatRoomId, sessionId, findMember);
        if (chatRoomService.isMemberInChatRoom(chatRoomId, token.getName())) {
            RedisChat makeChat = chatService.makeEnterOrLeaveChatMessage(MessageType.ENTER, chatRoomId, findMember);
            redisTemplate.convertAndSend(channelTopic.getTopic(), makeChat);
        }
    }

    /**
     * 구독 취소/커넥션 끊김 시 처리 메서드
     * @param sessionId 세션 식별자
     * @author mozzi327
     */
    public void unregisterUserAndSendLeaveMessage(String sessionId) {
        MemberInfoInChatRoom memberInfo = chatRoomService.deleteMemberFromChatRoom(sessionId);
        chatService.deleteLastSentInfo(sessionId);
        if (memberInfo.getMember() != null) {
            RedisChat messageToMember =
                    chatService.makeEnterOrLeaveChatMessage(MessageType.LEAVE, memberInfo.getChatRoomId(), memberInfo.getMember());
            redisTemplate.convertAndSend(channelTopic.getTopic(), messageToMember);
        }
    }

    /**
     * Authenticate 정보 조회 메서드
     * @param accessor StompHeaderAccessor
     * @author mozzi327
     */
    public void authenticate(StompHeaderAccessor accessor) {
        webSocketAuthService.authenticate(accessor);
    }
}
