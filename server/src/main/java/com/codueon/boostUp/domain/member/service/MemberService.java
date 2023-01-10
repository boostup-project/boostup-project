package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.PostAttemptFindPassword;
import com.codueon.boostUp.domain.member.dto.PostChangePassword;
import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.AccountStatus;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDbService memberDbService;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberRepository memberRepository;

    /**
     * 회원가입 메서드
     *
     * @param postMember 회원가입 정보
     * @author LimJaeminZ
     */
    public void createMember(PostMember postMember) {
        memberDbService.verifyName(postMember.getName());
        memberDbService.verifyEmail(postMember.getEmail());

        List<String> roles = authorityUtils.createRoles(postMember.getEmail());

        Member member = Member.builder()
                .email(postMember.getEmail())
                .password(memberDbService.encodingPassword(postMember.getPassword()))
                .name(postMember.getName())
                .accountStatus(AccountStatus.COMMON_MEMBER)
                .roles(roles)
                .build();

        memberDbService.saveMember(member);
    }

    /**
     * 사용자 본인 확인 조회 메서드
     * @param isRightMember 사용자 본인 확인 정보
     * @author mozzi327
     */
    public void isRightMember(PostAttemptFindPassword isRightMember) {
        memberDbService.isValidMember(isRightMember);
    }

    /**
     * password 변경 메서드
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    public void changePassword(PostChangePassword changePassword) {
        memberDbService.changingPassword(changePassword);
    }

    /**
     * 이메일 중복 확인 메서드
     * @param email 이메일 정보
     * @author mozzi327
     */
    public void checkIsOverLappedEmail(String email) {
        memberDbService.checkExistEmail(email);
    }

    /**
     * 닉네임 중복 확인 메서드
     * @param name 닉네임 정보
     * @author mozzi327
     */
    public void checkIsOverLappedName(String name) {
        memberDbService.checkExistName(name);
    }

    /**
     * 비밀번호 일치 확인 메서드
     * @param password 검증 비밀번호 정보
     * @param memberId 사용자 식별자
     * @author mozzi327
     */
    public void checkIsRightPassword(String password, Long memberId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        memberDbService.isValidPassword(findMember, password);
    }
}
