package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.*;
import com.codueon.boostUp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 이메일 중복 검사 컨트롤러 메서드
     * @param checkEmail 이메일 검사 정보
     * @author mozzi327
     */
    @PostMapping("/email/check")
    public ResponseEntity postCheckIsOverlappedEmail(@RequestBody PostEmail checkEmail) {
        memberService.checkIsOverLappedEmail(checkEmail.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * 닉네임 중복 검사 컨트롤러 메서드
     * @param checkName 닉네임 검사 정보
     * @author mozzi327
     */
    @PostMapping("/name/check")
    public ResponseEntity postCheckIsOverlappedName(@RequestBody PostName checkName) {
        memberService.checkIsOverLappedName(checkName.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 일치 여부 검사 컨트롤러 메서드
     * @param checkPassword 비밀번호 검사 정보
     * @author mozzi327
     */
    @PostMapping("/password/check")
    public ResponseEntity postCheckIsRightPassword(@RequestBody PostPassword checkPassword) {
        Long memberId = 1L;
        memberService.checkIsRightPassword(checkPassword.getPassword(), memberId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 본인 확인 컨트롤러 메서드
     * @param isRightUser 사용자 본인 확인 정보
     * @author mozzi327
     */
    @PostMapping("/password/find")
    public ResponseEntity postIsRightMember(@RequestBody PostAttemptFindPassword isRightUser) {
        memberService.isRightMember(isRightUser);
        return ResponseEntity.ok().build();
    }

    /**
     * 본인 확인 성공 시 비밀번호 변경 컨트롤러 메서드
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    @PostMapping("/password/resetting")
    public ResponseEntity postChangePassword(@RequestBody PostChangePassword changePassword) {
        memberService.changePassword(changePassword);
        return ResponseEntity.ok().build();
    }
}
