package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.AccountStatus;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDbService memberDbService;
    private final CustomAuthorityUtils authorityUtils;

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
}
