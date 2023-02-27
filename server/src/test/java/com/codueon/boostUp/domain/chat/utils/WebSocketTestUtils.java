package com.codueon.boostUp.domain.chat.utils;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public StompSession getSessionAfterConnect(WebSocketStompClient stompClient, String url,
                                               WebSocketHttpHeaders httpHeaders, StompHeaders stompHeaders) throws ExecutionException, InterruptedException, TimeoutException {
        ListenableFuture<StompSession> connection = stompClient
                .connect(url, httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {});
        return connection.get(2, TimeUnit.SECONDS);
    }

    public StompHeaders makeStompHeadersWithAccessToken(String token) {
        StompHeaders headers = new StompHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
