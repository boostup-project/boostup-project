package com.codueon.boostUp.domain.member.repository;

import com.codueon.boostUp.domain.member.entity.Member;

import java.util.Optional;

public interface CustomMemberRepository {
    Optional<Member> getNicknameByLessonId(Long lessonId);
}
