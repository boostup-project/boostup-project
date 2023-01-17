package com.codueon.chatserver.domain.chat.controller;

import com.codueon.chatserver.domain.chat.dto.Message;
import com.codueon.chatserver.domain.chat.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WSController {
    private final WebSocketService webSocketService;

    @PostMapping("/send/message")
    public void sendMessage(@RequestBody final Message message) {
        webSocketService.notifyFrontEnd(message.getMessageContent());
    }

    @PostMapping("/send/private/message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final Message message) {
        webSocketService.notifyUser(id, message.getMessageContent());
    }
}
