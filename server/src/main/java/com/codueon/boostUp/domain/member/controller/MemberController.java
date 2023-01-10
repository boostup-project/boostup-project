package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostAttemptFindPassword;
import com.codueon.boostUp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.dto.PatchMember;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody PostMember postMember) {
        memberService.createMember(postMember);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/profile")
    public ResponseEntity postProfileImage(@RequestBody PatchMember patchMember,
                                           @RequestPart(value = "file") MultipartFile file) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.noContent().build();
    }

    /**
     * 사용자 본인 확인 컨트롤러 메서드
     * @param isRightUser 사용자 본인 확인 정보
     * @author mozzi327
     */
    @PostMapping("/find/password")
    public ResponseEntity postIsRightMember(@RequestBody PostAttemptFindPassword isRightUser) {
        memberService.isRightMember(isRightUser);
        return ResponseEntity.ok().build();
    }
}
