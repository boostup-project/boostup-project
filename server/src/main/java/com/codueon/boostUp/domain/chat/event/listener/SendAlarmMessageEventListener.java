package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.event.vo.SendAlarmMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendEnterRoomMessageEvent;
import com.codueon.boostUp.domain.chat.event.vo.SendRegisterSuggestAlarmEvent;
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

    /**
     * 알람 메시지 전송 EventListener 메서드
     *
     * @param event 전송할 알람 메시지
     * @author mozzi327
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleChatRoomAlarm(SendAlarmMessageEvent event) {
        sendAlarmService.setAlarmAndSendAlarm(event.getSenderId(), event.getReceiverId(), event.getChatRoomId());
    }

    /**
     * 입장 시 알람 메시지 전송 EventListener 메서드
     *
     * @param event 입잡 시 알람 메시지
     * @author mozzi327
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEnterChatRoomAlarm(SendEnterRoomMessageEvent event) {
        sendAlarmService.sendEnterAlarm(event.getChatRoom(), event.getReceiverChat());
    }

    /**
     * 과외 신청 시 알람 메시지 전송 EventListener 메서드
     *
     * @param event 과외 신청 시 알람 메시지
     * @author mozzi327
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegisterSuggestAlarm(SendRegisterSuggestAlarmEvent event) {
    }
}
