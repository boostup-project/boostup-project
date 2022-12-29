package com.codueon.boostUp.domain.chat.controller;

import com.codueon.boostUp.domain.chat.dto.req.PostMessage;
import com.codueon.boostUp.domain.chat.entity.RedisChat;
import com.codueon.boostUp.domain.chat.redis.RedisPublisher;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatService chatService;
    private final RoomService roomService;

    @MessageMapping("/chats/message/{room-id}")
    public void chat(@DestinationVariable("room-id") Long roomId, PostMessage message) {
        LocalDateTime now = LocalDateTime.now();
        redisPublisher.publishingTopic(ChannelTopic.of("room" + roomId), new RedisChat(roomId, message.getSenderId(),
                message.getContent(), now));
        chatService.save(roomId, message, now);
    }
}
