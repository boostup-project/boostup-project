package com.codueon.boostUp.global.security.handler;


import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.utils.AuthConstants;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtTokenUtils jwtTokenUtils;


    /**
     * 로그인 성공 시 JWT 전송 메서드
     * @param request 요청 정보
     * @param response 응답 정보
     * @param authentication 생성된 Authentication 정보
     * @author LimJaeminZ
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new AuthException(ExceptionCode.MEMBER_NOT_FOUND));

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        String responseUri = createUri(accessToken, refreshToken, provider).toString();

        getRedirectStrategy().sendRedirect(request, response, responseUri);

    }

    private URI createUri(String accessToken, String refreshToken, String provider) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(AuthConstants.AUTHORIZATION, accessToken);
        queryParams.add(AuthConstants.REFRESH_TOKEN, refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                //.host("codueon.com")
                .host("localhost")
                .path("/oauth/" + provider)
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
