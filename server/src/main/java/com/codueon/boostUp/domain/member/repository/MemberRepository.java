package com.codueon.boostUp.domain.member.repository;

import com.codueon.boostUp.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    Boolean existsMemberByNameAndEmail(String name, String email);

    Boolean existsByEmail(String email);

    Boolean existsByName(String name);
}
