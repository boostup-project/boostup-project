package com.codueon.boostUp.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codueon.boostUp.domain.member.dto.PostLogin;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity postLoginMember(@RequestBody PostLogin login) {

        return ResponseEntity.ok().build();
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
