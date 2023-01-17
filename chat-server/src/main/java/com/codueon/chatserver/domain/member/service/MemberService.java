package com.codueon.chatserver.domain.member.service;

import com.codueon.chatserver.global.exception.BusinessLogicException;
import com.codueon.chatserver.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.codueon.chatserver.domain.member.entity.Member;
import com.codueon.chatserver.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member ifExistsReturnMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()
                -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
