package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.AuthDto;
import com.codueon.boostUp.domain.member.dto.PostLogin;
import com.codueon.boostUp.domain.member.dto.TokenDto;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberDbService memberDbService;
    private final JwtTokenUtils jwtTokenUtils;

    public TokenDto.Response loginMember(PostLogin login) {
        Member findMember = memberDbService.ifExistsMemberByEmail(login.getEmail());

        memberDbService.isValidPassword(findMember, login.getPassword());

        String generateAccessToken = jwtTokenUtils.generateAccessToken(findMember);
        String generateRefreshToken = jwtTokenUtils.generateRefreshToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", generateAccessToken);
        headers.add("refreshToken", generateRefreshToken);

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
