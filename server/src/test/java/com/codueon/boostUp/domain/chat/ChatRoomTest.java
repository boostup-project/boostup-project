package com.codueon.boostUp.domain.chat;

import com.codueon.boostUp.domain.chat.service.ChatAlarmService;
import com.codueon.boostUp.domain.chat.service.ChatRoomService;
import com.codueon.boostUp.domain.chat.service.ChatService;
import com.codueon.boostUp.domain.chat.utils.ChatMethodUtils;
import com.codueon.boostUp.domain.chat.utils.WebSocketTestUtils;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatRoomTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected ChatRoomService chatRoomService;
    @Autowired
    protected ChatAlarmService chatAlarmService;
    @Autowired
    protected MemberRepository memberRepository;
    protected String tutorToken;
    protected String studentToken;
    protected static ChatMethodUtils chatMethodUtils;

    @BeforeEach
    void setUp() {
        chatMethodUtils
                = new ChatMethodUtils(chatService, chatRoomService, chatAlarmService);
        chatMethodUtils.deleteAllRedisAfterTest();
    }
}
