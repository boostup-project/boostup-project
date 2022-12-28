package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDbService memberDbService;

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
}
