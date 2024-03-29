package com.codueon.boostUp.domain.member.repository;

import com.codueon.boostUp.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.member.entity.QMember.member;

@Repository
public class MemberRepositoryImpl implements CustomMemberRepository {
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 과외 선생님 정보 조회 메서드
     * @param lessonId 과외 식별자
     * @return Optional(Member)
     * @author mozzi327
     */
    @Override
    public Optional<Member> getMemberByLessonId(Long lessonId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .leftJoin(lesson).on(lesson.memberId.eq(member.id))
                .where(lesson.id.eq(lessonId))
                .fetchOne());
    }
}
