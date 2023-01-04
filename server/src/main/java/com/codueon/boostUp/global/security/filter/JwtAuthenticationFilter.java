package com.codueon.boostUp.global.security.filter;

import com.codueon.boostUp.domain.member.dto.PostLogin;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
        private final AuthenticationManager authenticationManager;
        private final JwtTokenUtils jwtTokenUtils;

        @SneakyThrows
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
                ObjectMapper objectMapper = new ObjectMapper();
                PostLogin postLogin = objectMapper.readValue(request.getInputStream(), PostLogin.class);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(postLogin.getEmail(), postLogin.getPassword());

                return authenticationManager.authenticate(authenticationToken);
        }

        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                FilterChain chain, Authentication authResult) {
                Member member = (Member) authResult.getPrincipal();

                String accessToken = delegateAccessToken(member);
                String refreshToken = delegateRefreshToken(member);
        }

        private String delegateAccessToken(Member member) {
                String accessToken = jwtTokenUtils.generateAccessToken(member);

                return accessToken;
        }

        private String delegateRefreshToken(Member member) {
                String refreshToken = jwtTokenUtils.generateRefreshToken(member);

                return refreshToken;
        }


}
