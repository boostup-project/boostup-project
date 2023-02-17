package com.codueon.boostUp.domain.chat;

import com.codueon.boostUp.domain.chat.dto.RedisChat;
import com.codueon.boostUp.domain.chat.utils.AlarmMessageUtils;
import com.codueon.boostUp.domain.chat.utils.StompFrameHandlerImpl;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;

//@MockBean(JpaMetamodelMappingContext.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChattingFullTest {

    @LocalServerPort
    protected int port;
    protected DataForTest data;
    protected ObjectMapper objectMapper;
    protected JwtTokenUtils jwtTokenUtils;
    protected AlarmMessageUtils alarmMessageUtils;
    protected WebSocketTestUtils webSocketTestUtils;

    protected Member sender;
    protected Member receiver;
    protected String url;
    protected BlockingQueue<RedisChat> receivedMessagesOfSender;
    protected BlockingQueue<RedisChat> receivedMessagesOfReceiver;
    protected String senderToken;
    protected String receiverToken;

    @BeforeEach
    void setUp() {
        data = new DataForTest();
        alarmMessageUtils = new AlarmMessageUtils();
        webSocketTestUtils = new WebSocketTestUtils();
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE, objectMapper);
        sender = data.getMember1();
        receiver = data.getMember2();
        url = "ws://localhost:" + port + "/ws/chat";
        receivedMessagesOfSender = new LinkedBlockingDeque<>();
        receivedMessagesOfReceiver = new LinkedBlockingDeque<>();
        senderToken = jwtTokenUtils.generateAccessToken(sender);
        receiverToken = jwtTokenUtils.generateAccessToken(receiver);
    }

    @Test
    void verifyGreetingIsReceived() throws Exception {
//        // 1. given
//        Long chatRoomId = 1L;
//        RedisChat sendChat = webSocketTestUtils.makeTalkRedisChat(sender.getId(), chatRoomId);
//
//        // 2. Set Connection For Receiver
//        StompHeaders receiverHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(receiverToken);
//        WebSocketStompClient receiverStompClient = webSocketTestUtils.makeStompClient();
//        StompSession receiverSession = webSocketTestUtils
//                .getSessionAfterConnect(receiverStompClient, url, new WebSocketHttpHeaders(), receiverHeaders);
//
//        // 3. Set Connection For Sender
//        StompHeaders senderHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(senderToken);
//        WebSocketStompClient senderStompClient = webSocketTestUtils.makeStompClient();
//        StompSession senderSession = webSocketTestUtils
//                .getSessionAfterConnect(senderStompClient, url, new WebSocketHttpHeaders(), senderHeaders);
//
//        // 4. Receiver Subscribe
//        receiverSession.subscribe(
//                String.format("/topic/rooms/%d", chatRoomId),
//                new StompFrameHandlerImpl(new RedisChat(), receivedMessagesOfReceiver)
//        );
//        RedisChat receiverMessageAfterReceiverSubscribe = receivedMessagesOfReceiver.poll(3, TimeUnit.SECONDS);
//        RedisChat senderMessageAfterReceiverSubscribe = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);
//
//        // 5. Sender Subscribe
//        senderSession.subscribe(
//                String.format("/topic/rooms/%d", chatRoomId),
//                new StompFrameHandlerImpl(new RedisChat(), receivedMessagesOfSender)
//        );
//        RedisChat receiverMessageAfterSenderSubscribe = receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
//        RedisChat senderMessageAfterSenderSubscribe = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);


    }
}
