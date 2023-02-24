package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.event.vo.AlarmChatListEvent;
import com.codueon.boostUp.domain.chat.event.vo.InitialChatRoomListEvent;
import com.codueon.boostUp.domain.chat.service.EventAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNewChatAlarmEventListener {
    private final EventAlarmService sendAlarmService;

    /**
     * 알람 메시지 전송 EventListener 메서드
     *
     * @param event 전송할 알람 메시지
     * @author mozzi327
     */
    @EventListener
    public void handleChatRoomAlarm(AlarmChatListEvent event) {
        sendAlarmService.setAlarmAndSendAlarm(event.getMemberId(), event.getChatRoomId(), event.isReceiver());
    }

    /**
     * 입장 시 알람 메시지 전송 EventListener 메서드
     *
     * @param event 입잡 시 알람 메시지
     * @author mozzi327
     */
    @EventListener
    public void handleEnterChatRoomAlarm(InitialChatRoomListEvent event) {
        sendAlarmService.sendEnterAlarm(event.getChatRoom(), event.getReceiverChat());
    }
}
