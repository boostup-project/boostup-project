package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate template;

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");
        template.convertAndSend("/topic/global/notifications", message);
    }

    public void sendPrivateNotification(final String userId) {
        ResponseMessage message = new ResponseMessage("Private Notification");
        template.convertAndSendToUser(userId, "/topic/private/notifications", message);
    }
}
