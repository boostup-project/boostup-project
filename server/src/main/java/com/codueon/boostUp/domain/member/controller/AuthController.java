package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.TokenDto;
import com.codueon.boostUp.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codueon.boostUp.domain.member.dto.PostLogin;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity postLoginMember(@RequestBody PostLogin login) {
        TokenDto.Response response = authService.loginMember(login);
        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getResponse());
    }

    @DeleteMapping("/logout")
    public ResponseEntity deleteLoginMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @GetMapping("/re-issue")
    public ResponseEntity getReIssueToken(@RequestHeader("authorization") String accessToken,
                                          @RequestHeader("refreshToken") String refreshToken) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }
}
