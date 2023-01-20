package com.codueon.boostUp.domain.chat.service;

import com.codueon.boostUp.domain.chat.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public void notifyFrontEnd(final String message) {
        ResponseMessage responseMessage = new ResponseMessage(message);
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/messages", responseMessage);
    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage responseMessage = new ResponseMessage(message);
        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private/messages", responseMessage);
    }
}
