package com.codueon.boostUp.domain.chat.event.listener;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.event.vo.SendEnterMessageEvent;
import com.codueon.boostUp.domain.chat.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageEventListener {
    private final SendMessageService sendChatService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleSendMessage(RedisChat redisChat) {
        sendChatService.sendMessage(redisChat);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendEnterMessage(SendEnterMessageEvent event) {
        sendChatService.sendEnterMessage(
                event.getChatRoomId(),
                event.getSenderChat(),
                event.getReceiverChat()
        );
    }
}
