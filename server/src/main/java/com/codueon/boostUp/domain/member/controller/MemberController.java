package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostEmail;
import com.codueon.boostUp.domain.member.dto.PostPassword;
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

    /**
     * 회원가입 컨트롤러 메서드
     * @param postMember 회원가입 정보
     * @author LimJaeMinZ
     */
    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody PostMember postMember) {
        memberService.createMember(postMember);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 회원 수정 컨트롤러 메서드
     * @param patchMember 회원 수정 정보
     * @param memberImage 회원 사진 정보
     * @author mozzi327
     */
    @PostMapping("/modification")
    public ResponseEntity postEditMemberProfile(@RequestBody PatchMember patchMember,
                                           @RequestPart(value = "file") MultipartFile memberImage) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
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
     * 비밀번호 변경 컨트롤러 메서드(로그인페이지)
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    @PostMapping("/password/resetting")
    public ResponseEntity postChangePasswordInLoginPage(@RequestBody PostPasswordInLoginPage changePassword) {
        memberService.changePasswordInLoginPage(changePassword);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 변경 컨트롤러 메서드(마이페이지)
     * @param changePassword 비밀번호 변경 정보
     * @return
     * @author mozzi327
     */
    @PostMapping("/password/resetting/my-page")
    public ResponseEntity postChangePasswordInMyPage(@RequestBody PostPasswordInMyPage changePassword) {
        Long memberId = 1L;
        memberService.changePasswordInMyPage(memberId, changePassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.noContent().build();
    }
}
