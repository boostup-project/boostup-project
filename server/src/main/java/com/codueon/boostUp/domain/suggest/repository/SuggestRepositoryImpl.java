package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTutorSuggest;
import com.codueon.boostUp.domain.suggest.dto.QGetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.QGetTutorSuggest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.member.entity.QMember.member;
import static com.codueon.boostUp.domain.suggest.entity.QSuggest.suggest;
import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.*;

public class SuggestRepositoryImpl implements CustomSuggestRepository{
    private final JPAQueryFactory queryFactory;

    public SuggestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetTutorSuggest> getTutorSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable) {
        List<GetTutorSuggest> results = queryFactory
                .select(new QGetTutorSuggest(
                        suggest.id,
                        suggest.days,
                        suggest.languages,
                        suggest.requests,
                        suggest.suggestStatus,
                        suggest.startTime,
                        suggest.endTime,
                        lesson.id,
                        member.name
                ))
                .from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .leftJoin(member).on(suggest.memberId.eq(member.id))
                .where( lesson.memberId.eq(memberId),
                        suggest.lessonId.eq(lessonId),
                        changeStatusByTabId(tabId))
                .orderBy(suggest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression changeStatusByTabId(int tabId) {
        switch (tabId) {
            case 1: return suggest.suggestStatus.eq(ACCEPT_IN_PROGRESS).or(
                           suggest.suggestStatus.eq(PAY_IN_PROGRESS));
            case 2: return suggest.suggestStatus.eq(DURING_LESSON);
            default: return suggest.suggestStatus.eq(END_OF_LESSON).or(
                            suggest.suggestStatus.eq(REFUND_PAYMENT));
        }
    }

    @Override
    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        List<GetStudentSuggest> results = queryFactory
                .select(new QGetStudentSuggest(
                        suggest.id,
                        member.name,
                        suggest.suggestStatus,
                        suggest.startTime,
                        suggest.endTime,
                        lesson
                ))
                .from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(suggest.memberId.eq(memberId))
                .orderBy(suggest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }
}
