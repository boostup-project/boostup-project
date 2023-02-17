package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatAlarm;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatMessage;
import com.codueon.boostUp.domain.chat.repository.redis.RedisChatRoom;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketTestUtils {

    public WebSocketStompClient makeStompClient() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = List.of(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient client = new WebSocketStompClient(sockJsClient);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        return client;
    }

    public RedisChat makeTalkRedisChat(Long memberId, Long chatRoomId) {
        return RedisChat.builder()
                .chatRoomId(chatRoomId)
                .senderId(memberId)
                .displayName("길동이에요")
                .message("테스트 메시지입니다.")
                .messageType(MessageType.TALK)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @SneakyThrows
    public StompSession getSessionAfterConnect(WebSocketStompClient stompClient, String url,
                                               WebSocketHttpHeaders httpHeaders, StompHeaders stompHeaders) {
        ListenableFuture<StompSession> connection = stompClient
                .connect(url, httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
                });
        return connection.get(100, TimeUnit.SECONDS);
    }

    public StompHeaders makeStompHeadersWithAccessToken(String token) {
        StompHeaders headers = new StompHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
