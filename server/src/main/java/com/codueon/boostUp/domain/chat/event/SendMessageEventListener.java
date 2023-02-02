package com.codueon.boostUp.domain.chat.event;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageEventListener {
    private final ChatService chatService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendMessage(SendMessageEvent sendMessageEvent) {
        chatService.sendEnterMessage(sendMessageEvent.getChatRoomId(), sendMessageEvent.getSenderChat(), sendMessageEvent.getReceiverChat());
    }
}
