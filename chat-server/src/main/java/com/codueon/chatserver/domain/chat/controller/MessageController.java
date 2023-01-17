package com.codueon.chatserver.domain.chat.controller;

import com.codueon.chatserver.domain.chat.dto.Message;
import com.codueon.chatserver.domain.chat.dto.ResponseMessage;
import com.codueon.chatserver.domain.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    @SneakyThrows
    public ResponseMessage getMessage(final Message message) {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private/message")
    @SendToUser("/topic/private/messages")
    @SneakyThrows
    public ResponseMessage getPrivateMessage(final Message message,
                                             final Principal principal) {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils
                .htmlEscape("Sending private message to user "
                        + principal.getName()+ ": "
                        + message.getMessageContent()));
    }
}
