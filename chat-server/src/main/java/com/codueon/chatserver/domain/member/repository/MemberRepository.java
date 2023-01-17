package com.codueon.chatserver.domain.member.repository;

import com.codueon.chatserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
