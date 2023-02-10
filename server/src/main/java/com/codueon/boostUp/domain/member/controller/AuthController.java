package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.TokenDto;
import com.codueon.boostUp.domain.member.service.AuthService;
import com.codueon.boostUp.domain.vo.AuthVO;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.codueon.boostUp.domain.member.dto.PostLogin;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 로그인
     * @param login 로그인 정보
     * @return ResponseEntity
     * @author LimJaeminZ
     */
    @PostMapping("/login")
    public ResponseEntity postLoginMember(@RequestBody @Valid PostLogin login) {
        TokenDto.Response response = authService.loginMember(login);
        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getResponse());
    }

    /**
     * 사용자 로그아웃
     * @param accessToken 엑세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param authentication 사용자 인증 정보
     * @author LimJaeminZ
     */
    @DeleteMapping("/logout")
    public ResponseEntity deleteLoginMember(@RequestHeader("authorization") String accessToken,
                                            @RequestHeader("refreshToken") String refreshToken,
                                            Authentication authentication) {
        authService.logoutMember(accessToken, refreshToken, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().build();
    }

    /**
     * 토큰 재발급
     * @param accessToken 엑세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author LimJaeminZ
     */
    @GetMapping("/re-issue")
    public ResponseEntity getReIssueToken(@RequestHeader("authorization") String accessToken,
                                          @RequestHeader("refreshToken") String refreshToken,
                                          Authentication authentication) {
        TokenDto.Response response = authService.reIssueToken(accessToken, refreshToken, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getResponse());
    }
}
