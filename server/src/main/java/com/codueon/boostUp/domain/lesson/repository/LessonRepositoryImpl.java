package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.dto.GetStudentLesson;
import com.codueon.boostUp.domain.lesson.dto.QGetMainPageLesson;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;

public class LessonRepositoryImpl implements CustomLessonRepository {

    private final JPAQueryFactory queryFactory;

    public LessonRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 메인페이지 DB 조회 QueryDsl(로그인 X)
     *
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getMainPageLessons(Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lesson,
                        Expressions.constant(false)
                ))
                .from(lesson)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 메인페이지 DB 조회 QueryDsl(로그인 O)
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getMainPageLessonsAndBookmarkInfo(Long memberId, Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lesson,
                        isBookmarked(lesson.id, memberId)
                ))
                .from(lesson)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 북마크 여부 조회 서브 쿼리
     * @param lessonId 과외 식별자(NumberPath)
     * @param memberId 사용자 식별자
     * @return JPQLQuery(북마크 여부)
     * @author mozzi327
     */
    private JPQLQuery<Boolean> isBookmarked(NumberPath<Long> lessonId, Long memberId) {
        return JPAExpressions.select(
                new CaseBuilder()
                    .when(bookmark.isNotNull()).then(true)
                    .otherwise(false)
                ).from(bookmark)
                .where(bookmark.lessonId.eq(lessonId).and(bookmark.memberId.eq(memberId)));
    }

    /**
     * 마이페이지 과외 정보 조회 서브 쿼리
     * @param memberId
     * @param pageable
     * @return
     */
    @Override
    public Page<GetStudentLesson> getMyPageLessonInfo(Long memberId, Pageable pageable) {
        return null;
    }
}
