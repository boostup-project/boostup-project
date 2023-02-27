package com.codueon.boostUp.domain.chat;

import com.codueon.boostUp.domain.IntegrationTest;
import com.codueon.boostUp.domain.chat.service.WebSocketAuthService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.domain.vo.AuthInfo;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.Principal;
import java.security.SignatureException;

import static com.codueon.boostUp.domain.chat.utils.DataForChat.*;
import static com.codueon.boostUp.domain.member.entity.AccountStatus.COMMON_MEMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebSocketAuthServiceTest extends IntegrationTest {
    @Autowired
    protected RedisUtils redisUtils;
    @Autowired
    protected JwtTokenUtils jwtTokenUtils;
    @Autowired
    protected WebSocketAuthService webSocketAuthService;
    protected StompHeaderAccessor accessor;
    protected Member tutor;
    protected String accessToken;
    protected String refreshToken;
    protected MultiValueMap<String, String> headers;

    @BeforeAll
    void beforeAll() {
        tutor = DataForTest.getSavedTutor();
    }

    @BeforeEach
    void setUp() {
        headers = new LinkedMultiValueMap<>();
        refreshToken = jwtTokenUtils.generateRefreshToken(tutor);
    }

    @AfterEach
    void afterEach() {
        redisUtils.deleteData(tutor.getEmail(), "common");
    }

    @Test
    @DisplayName("액세스 토큰이 만료되지 않았을 경우, Accessor에 정상적으로 Authentication을 싣는다.")
    void authenticateNotExpiredTokenTest() {
        // given
        accessToken = jwtTokenUtils.generateAccessToken(tutor);
        String authorization = "Authorization";
        headers.set(authorization, "Bearer " + accessToken);
        accessor = StompHeaderAccessor.create(StompCommand.CONNECT, headers);
        redisUtils.setData(
                tutor.getEmail(), COMMON_MEMBER.getProvider(), refreshToken,
                jwtTokenUtils.getRefreshTokenExpirationMinutes()
        );

        // when
        webSocketAuthService.authenticate(accessor);

        // then
        Principal user = accessor.getUser();
        assertThat(user).isNotNull();
        JwtAuthenticationToken token = (JwtAuthenticationToken) user;
        assertThat(token.getId()).isEqualTo(TUTOR_ID);
        assertThat(token.getName()).isEqualTo(TUTOR_NAME);

        // 재발급 여부 확인
        assertThat(token.isExpired()).isFalse();
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("액세스 토큰이 만료되었을 경우 RefreshToken이 존재하는지 확인한 후, " +
            "존재할 경우 Accessor에 Authentication을 정상적으로 싣는다.")
    void authenticateExpiredTokenTest() throws Exception {
        // given
        accessToken = EXPIRED_ACCESS_TOKEN;
        String authorization = "Authorization";
        headers.set(authorization, "Bearer " + accessToken);
        accessor = StompHeaderAccessor.create(StompCommand.CONNECT, headers);
        redisUtils.setData(
                tutor.getEmail(), COMMON_MEMBER.getProvider(), refreshToken,
                jwtTokenUtils.getRefreshTokenExpirationMinutes()
        );

        // when
        webSocketAuthService.authenticate(accessor);

        // then
        Principal user = accessor.getUser();
        assertThat(user).isNotNull();
        JwtAuthenticationToken token = (JwtAuthenticationToken) user;
        assertThat(token.getId()).isEqualTo(TUTOR_ID);
        assertThat(token.getName()).isEqualTo(TUTOR_NAME);

        // 재발급 여부 확인
        assertThat(token.isExpired()).isTrue();
        assertThat(token.getAccessToken()).isNotEqualTo(accessToken);
    }

    @Test
    @DisplayName("액세스 토큰이 유효하지 않을 경우 예외를 던진다.")
    void authenticateInvalidTokenExceptionTest() throws Exception {
        // given
        accessToken = "thisIsFakeAccessToken";
        String authorization = "Authorization";
        headers.set(authorization, "Bearer " + accessToken);
        accessor = StompHeaderAccessor.create(StompCommand.CONNECT, headers);
        redisUtils.setData(
                tutor.getEmail(), COMMON_MEMBER.getProvider(), refreshToken,
                jwtTokenUtils.getRefreshTokenExpirationMinutes()
        );

        // when and then
        assertThatThrownBy(
                () -> webSocketAuthService.authenticate(accessor)
        ).isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("액세스 토큰이 만료되었고, 리프레시 토큰이 Redis 저장소에 존재하지 않을 경우 예외를 던진다.")
    void authenticateExpiredTokenNotFoundRefreshCaseTest() throws Exception {
        // given
        accessToken = EXPIRED_ACCESS_TOKEN;
        String authorization = "Authorization";
        headers.set(authorization, "Bearer " + accessToken);
        accessor = StompHeaderAccessor.create(StompCommand.CONNECT, headers);

        // when and then
        assertThatThrownBy(
                () -> webSocketAuthService.authenticate(accessor)
        ).isInstanceOf(AuthException.class);
    }
}
