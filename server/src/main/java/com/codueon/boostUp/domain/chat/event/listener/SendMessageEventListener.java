package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendSuggestMessageEvent;
import com.codueon.boostUp.domain.chat.service.EventMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageEventListener {
    private final EventMessageService sendChatService;

    /**
     * 메시지 전송 알람 EventListener 메서드
     *
     * @param redisChat 전송 메시지
     * @author mozzi327
     */
    @EventListener
    public void handleSendMessage(RedisChat redisChat) {
        sendChatService.sendMessage(redisChat);
    }

    /**
     * 입장 시 입장 메시지 전송 EventListener 메서드
     *
     * @param event 입장 메시지
     * @author mozzi327
     */
    @EventListener
    public void handleSendEnterMessage(InitialChatRoomMessageEvent event) {
        sendChatService.sendEnterMessage(
                event.getChatRoomId(),
                event.getEnterChat(),
                event.getCount()
        );
    }

    /**
     * 과외 신청 프로세스에 대한 알람 메시지 전송 EventListener 메서드
     *
     * @param event 알람 전송 이벤트
     * @author mozzi327
     */
    @EventListener
    public void handleSendAlarmMessage(SendSuggestMessageEvent event) {
        sendChatService.sendAlarmChannelMessage(event.getMemberId(), event.getLessonTitle(), event.getDisplayName(),
                event.getAttendanceCount(), event.getMessage(), event.getAlarmType());
    }
}
