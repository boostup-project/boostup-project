package com.codueon.boostUp.domain.member.auth;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenUtilsTest {
    @MockBean
    JwtTokenUtils jwtTokenUtils;
    private String secretKey = "admin123456789123456789123456789";
    private String base64EncodedSecretKey;
    private int accessTokenExpirationsMinutes = 30;
    private int refreshTokenExpirationMinutes = 1440;
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        jwtTokenUtils = new JwtTokenUtils(secretKey, accessTokenExpirationsMinutes, refreshTokenExpirationMinutes, objectMapper);
        base64EncodedSecretKey = jwtTokenUtils.encodeBase64SecretKey(secretKey);
    }

    @Test
    @DisplayName("secret Key -> Base64 인코딩 Test")
    public void encodeBase64SecretKeyTest() {
        System.out.println("base64EncodedSecretKey : " + base64EncodedSecretKey);

        assertThat(secretKey, is(new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    @Test
    @DisplayName("access Token Test")
    public void generateAccessTokenTest() {
        Member member = Member.builder()
                .id(1L)
                .email("hdg@gmail.com")
                .password("ghdrlfehd")
                .name("홍길동")
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);

        System.out.println("accessToken : " + accessToken);

        assertThat(accessToken, notNullValue());
    }

    @Test
    @DisplayName("refresh Token Test")
    public void generateRefreshToken() {
        Member member = Member.builder()
                .id(1L)
                .email("hdg@gmail.com")
                .password("ghdrlfehd")
                .name("홍길동")
                .build();

        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        System.out.println("refreshToken : " + refreshToken);

        assertThat(refreshToken, notNullValue());
    }
}
