package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostEmail;
import com.codueon.boostUp.domain.member.dto.PostPassword;
import com.codueon.boostUp.domain.member.dto.*;
import com.codueon.boostUp.domain.member.service.MemberService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    public ResponseEntity postMember(@RequestBody @Valid PostMember postMember) {
        memberService.createMember(postMember);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 회원 정보 수정 컨트롤러 메서드
     * @param name 닉네임 정보
     * @param file 프로필 이미지 정보
     * @author LeeGoh
     */
    @PostMapping("/test/modification")
    public ResponseEntity postEditMemberInfoInMyPage(@RequestPart(value = "data") PostName name,
                                                     @RequestPart(value = "profileImage") MultipartFile file,
                                                     Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok().body(memberService.editMemberInfo(name, file, memberId));
    }

    /**
     * 회원 정보 수정 컨트롤러 메서드
     * @param name 닉네임 정보
     * @param file 프로필 이미지 정보
     * @author LeeGoh
     */
    @PostMapping("/modification")
    public ResponseEntity postEditMemberInfoInMyPageS3(@RequestPart(value = "data") PostName name,
                                                       @RequestPart(value = "profileImage") MultipartFile file,
                                                       Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok().body(memberService.editMemberInfoS3(name, file, memberId));
    }

    /**
     * 이메일 중복 검사 컨트롤러 메서드
     * @param checkEmail 이메일 검사 정보
     * @author mozzi327
     */
    @PostMapping("/email/overlap/check")
    public ResponseEntity postCheckIsOverlappedEmail(@RequestBody PostEmail checkEmail) {
        memberService.checkIsOverLappedEmail(checkEmail.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * DB 이메일 존재 여부 확인 컨트롤러 메서드
     * @param checkEmail 이메일 검사 정보
     * @author mozzi327
     */
    @PostMapping("/email/check")
    public ResponseEntity postCheckIsExistEmailInDb(@RequestBody @Valid PostEmail checkEmail) {
        memberService.checkIsExistEmailInDb(checkEmail.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * 닉네임 중복 검사 컨트롤러 메서드
     * @param checkName 닉네임 검사 정보
     * @author mozzi327
     */
    @PostMapping("/name/overlap/check")
    public ResponseEntity postCheckIsOverlappedName(@RequestBody @Valid PostName checkName) {
        memberService.checkIsOverLappedName(checkName.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 일치 여부 검사 컨트롤러 메서드
     * @param checkPassword 비밀번호 검사 정보
     * @author mozzi327
     */
    @PostMapping("/password/check")
    public ResponseEntity postCheckIsRightPassword(@RequestBody PostPassword checkPassword,
                                                   Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        memberService.checkIsRightPassword(checkPassword.getPassword(), memberId);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 변경 컨트롤러 메서드(로그인페이지)
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    @PostMapping("/password/resetting")
    public ResponseEntity postChangePasswordInLoginPage(@RequestBody @Valid PostPasswordInLoginPage changePassword) {
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
    public ResponseEntity postChangePasswordInMyPage(@RequestBody @Valid PostPasswordInMyPage changePassword,
                                                     Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        memberService.changePasswordInMyPage(memberId, changePassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deleteMember() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그인 확인 메서드
     * @param token 토큰 정보
     * @return Long
     * @author mozzi327
     */
    private Long getMemberIdIfExistToken(JwtAuthenticationToken token) {
        if (token == null) return null;
        else return token.getId();
    }
}
