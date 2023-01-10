package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.PostAttemptFindPassword;
import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDbService memberDbService;
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

        Member member = Member.builder()
                .email(postMember.getEmail())
                .password(memberDbService.encodingPassword(postMember.getPassword()))
                .name(postMember.getName())
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
}
