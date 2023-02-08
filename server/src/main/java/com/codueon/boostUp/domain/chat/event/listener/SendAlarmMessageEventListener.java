package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.event.vo.SendAlarmMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendEnterRoomMessageEvent;
import com.codueon.boostUp.domain.chat.service.SendAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendAlarmMessageEventListener {
    private final SendAlarmService sendAlarmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleChatRoomAlarm(SendAlarmMessageEvent event) {
        sendAlarmService.setAlarmAndSendAlarm(event.getSenderId(), event.getReceiverId(), event.getChatRoomId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEnterChatRoomAlarm(SendEnterRoomMessageEvent event) {
        sendAlarmService.sendEnterAlarm(event.getChatRoom(), event.getReceiverChat());
    }
}
