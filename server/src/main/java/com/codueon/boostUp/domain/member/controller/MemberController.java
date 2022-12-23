package com.codueon.boostUp.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.dto.PatchMember;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody PostMember postMember) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity patchMember(@RequestBody PatchMember patchMember) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile")
    public ResponseEntity postProfileImage(@RequestPart(value = "file") MultipartFile file) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.noContent().build();
    }
}
