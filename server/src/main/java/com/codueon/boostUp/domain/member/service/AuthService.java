package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.AuthDto;
import com.codueon.boostUp.domain.member.dto.PostLogin;
import com.codueon.boostUp.domain.member.dto.TokenDto;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.global.security.utils.AuthConstants.AUTHORIZATION;
import static com.codueon.boostUp.global.security.utils.AuthConstants.REFRESH_TOKEN;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberDbService memberDbService;
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;

    public TokenDto.Response loginMember(PostLogin login) {
        Member findMember = memberDbService.ifExistsMemberByEmail(login.getEmail());

        memberDbService.isValidPassword(findMember, login.getPassword());

        String generateAccessToken = jwtTokenUtils.generateAccessToken(findMember);
        String generateRefreshToken = jwtTokenUtils.generateRefreshToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateAccessToken);
        headers.add(REFRESH_TOKEN, generateRefreshToken);

        //redis 저장
        redisUtils.setData(generateRefreshToken, findMember.getId(), jwtTokenUtils.getRefreshTokenExpirationMinutes());

        AuthDto memberRes = AuthDto.builder()
                .email(findMember.getEmail())
                .name(findMember.getName())
                .build();

        return TokenDto.Response.builder()
                .response(memberRes)
                .headers(headers)
                .build();
    }
}
