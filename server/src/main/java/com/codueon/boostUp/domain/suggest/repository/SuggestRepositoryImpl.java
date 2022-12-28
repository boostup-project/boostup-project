package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTeacherSuggest;
import com.codueon.boostUp.domain.suggest.dto.QGetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.QGetTeacherSuggest;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.suggest.entity.QSuggest.suggest;

public class SuggestRepositoryImpl implements CustomSuggestRepository{

    private final JPAQueryFactory queryFactory;

    public SuggestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetTeacherSuggest> getTeacherSuggestsOnMyPage(Long lessonId, Long memberId, Long tabId, Pageable pageable) {
        List<GetTeacherSuggest> results = queryFactory
                .select(new QGetTeacherSuggest(
                        suggest,
                        lesson.id,
                        lesson.name
                ))
                .from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
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

    private BooleanExpression changeStatusByTabId(Long tabId) {
        if(tabId == 1) return suggest.status.eq(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS);
        else if(tabId == 2) {
            return suggest.status.eq(Suggest.SuggestStatus.PAY_IN_PROGRESS).and(
                    suggest.status.eq(Suggest.SuggestStatus.DURING_LESSON));
        } else return suggest.status.eq(Suggest.SuggestStatus.END_OF_LESSON);
    }

    @Override
    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        List<GetStudentSuggest> results = queryFactory
                .select(new QGetStudentSuggest(
                        suggest,
                        lesson
                ))
                .from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .where(suggest.memberId.eq(memberId))
                .orderBy(suggest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }



}
