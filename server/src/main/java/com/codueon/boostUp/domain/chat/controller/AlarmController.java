package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.vo.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {
    private final ChatAlarmService chatAlarmService;

    /**
     * 채팅방 입장 시 알람 초기화 컨트롤러 메서드
     * @param chatRoomId 채팅방 식별자
     * @param receiverId 사용자 식별자(Receiver)
     * @author mozzi327
     */
    @GetMapping("/alarm/room/{room-id}")
    public ResponseEntity enterChatRoom(@PathVariable("room-id") Long chatRoomId,
                                        Authentication authentication) {
        chatAlarmService.initAlarm(AuthInfo.ofMemberId(authentication), chatRoomId);
        return ResponseEntity.noContent().build();
    }
}
