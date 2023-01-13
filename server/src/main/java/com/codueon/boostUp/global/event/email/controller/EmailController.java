package com.codueon.boostUp.global.event.email.controller;

import com.codueon.boostUp.global.event.email.dto.PostEmailCode;
import com.codueon.boostUp.global.event.email.service.EmailService;
import com.codueon.boostUp.domain.member.dto.PostEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/code/transmission")
    public ResponseEntity postEmailAuthorizationCode(@RequestBody PostEmail email) {
        emailService.sendAuthorizationCodeToMemberEmail(email.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/code/validation")
    public ResponseEntity postValidEmailAuthorizationCode(@RequestBody PostEmailCode emailCode) {
        emailService.isRightEmailAuthorizationCode(emailCode);
        return ResponseEntity.ok().build();
    }
}
