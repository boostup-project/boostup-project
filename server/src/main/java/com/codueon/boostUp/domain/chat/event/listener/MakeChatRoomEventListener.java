package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.event.vo.*;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeChatRoomEventListener {
    private final ChatRoomService chatRoomService;

    /**
     * 회원가입 시 알림 채팅방 생성 EventListener 메서드
     *
     * @param event 알림 채팅방 생성 이벤트
     * @author mozzi327
     */
    @EventListener
    public void handleSendJoinMemberMessage(MakeAlarmChatRoomEvent event) {
        chatRoomService.createAlarmChatRoom(event.getMemberId(), event.getMemberNickname());
    }

    /**
     * 과외 신청 시 유저 간 채팅방 생성 EventListener 메서드
     *
     * @param event 유저 채팅방 생성 이벤트
     * @author mozzi327
     */
    @EventListener
    public void handleMakeChatRoomWhenRegisterSuggest(MakeChatRoomEvent event) {
        chatRoomService.createChatRoom(event.getAuthInfo(), event.getLessonId());
    }
}
