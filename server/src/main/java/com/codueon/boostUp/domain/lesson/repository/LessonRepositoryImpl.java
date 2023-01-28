package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.entity.AddressInfo;
import com.codueon.boostUp.domain.lesson.entity.LanguageInfo;
import com.codueon.boostUp.domain.lesson.entity.QLesson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.lesson.entity.QLessonAddress.lessonAddress;
import static com.codueon.boostUp.domain.lesson.entity.QLessonLanguage.lessonLanguage;

import static com.codueon.boostUp.domain.member.entity.QMember.member;


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
                        member.name,
                        Expressions.constant(false)
                ))
                .from(lesson,member)
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
    public Page<GetMainPageLesson> getMainPageLessonsAndBookmarkInfo(Long memberId,
                                                                     Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lesson,
                        member.name,
                        isBookmarked(lesson.id, memberId)
                ))
                .from(lesson, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 언어 별 메인페이지 조회 QueryDsl(로그인 X)
     *
     * @param languageId 사용 언어 식별자
     * @param pageable   페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getMainPageLessonByLanguage(Integer languageId,
                                                               Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lessonLanguage.lesson,
                        member.name,
                        Expressions.constant(false)
                )).from(lessonLanguage, member)
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .where(lessonLanguage.languageInfo.eq(LanguageInfo.findById(languageId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lessonLanguage.lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 언어 별 메인페이지 조회 QueryDsl(로그인 O)
     *
     * @param memberId   사용자 식별자
     * @param languageId 사용 언어 식별자
     * @param pageable   페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getMainPageLessonByLanguageAndBookmarkInfo(Long memberId,
                                                                              Integer languageId,
                                                                              Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lessonLanguage.lesson,
                        member.name,
                        isBookmarked(lessonLanguage.lesson.id, memberId)
                )).from(lessonLanguage, member)
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .where(lessonLanguage.languageInfo.eq(LanguageInfo.findById(languageId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lessonLanguage.lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 상세 검색 조회 QueryDsl(로그인 X)
     *
     * @param postSearchLesson 상세 검색 정보
     * @param pageable         페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getDetailSearchMainPageLesson(PostSearchLesson postSearchLesson,
                                                                 Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lesson,
                        member.name,
                        Expressions.constant(false)
                )).from(lesson,member)
                .leftJoin(lessonAddress).on(lesson.id.eq(lessonAddress.lesson.id))
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .where(makeDetailSearchConditions(postSearchLesson))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 상세 검색 조회 QueryDsl(로그인 O)
     *
     * @param memberId         사용자 식별자
     * @param postSearchLesson 상세 검색 정보
     * @param pageable         페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @Override
    public Page<GetMainPageLesson> getDetailSearchMainPageLessonAndGetBookmarkInfo(Long memberId,
                                                                                   PostSearchLesson postSearchLesson,
                                                                                   Pageable pageable) {
        List<GetMainPageLesson> result = queryFactory
                .select(new QGetMainPageLesson(
                        lesson,
                        member.name,
                        isBookmarked(lesson.id, memberId)
                )).from(lesson, member)
                .leftJoin(lessonAddress).on(lesson.id.eq(lessonAddress.lesson.id))
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .where(makeDetailSearchConditions(postSearchLesson))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    public GetLesson getDetailLesson(Long lessonId) {
        GetLesson result = queryFactory
                .select(new QGetLesson(
                        lesson,
                        member
                )).from(lesson, member)
                .fetchOne();
        return result;
    }

    /**
     * 상세 검색 조건 쿼리 생성 메서드
     *
     * @param postSearchLesson 상세 검색 정보
     * @return BooleanBuilder
     * @author mozzi327
     */
    private BooleanBuilder makeDetailSearchConditions(PostSearchLesson postSearchLesson) {
        BooleanBuilder builder = new BooleanBuilder();

        if (postSearchLesson.getAddress() != null && postSearchLesson.getLanguage() != null)
            builder.and(lessonLanguage.lesson.id.eq(lessonAddress.lesson.id));

        if (postSearchLesson.getAddress() != null)
            builder.and(lessonAddress.addressInfo.eq(AddressInfo.findById(postSearchLesson.getAddress())));

        if (postSearchLesson.getLanguage() != null)
            builder.and(lessonLanguage.languageInfo.eq(LanguageInfo.findById(postSearchLesson.getLanguage())));

        if (postSearchLesson.getName() != null)
            builder.and(member.name.contains(postSearchLesson.getName()));

        if (postSearchLesson.getCareer() != null)
            builder.and(lesson.career.goe(postSearchLesson.getCareer()));

        if (postSearchLesson.getStartCost() != null && postSearchLesson.getEndCost() != null)
            return builder.and(lesson.cost.between(postSearchLesson.getStartCost(), postSearchLesson.getEndCost()));
        else if (postSearchLesson.getStartCost() != null)
            return builder.and(lesson.cost.goe(postSearchLesson.getStartCost()));
        else if (postSearchLesson.getEndCost() != null)
            return builder.and(lesson.cost.loe(postSearchLesson.getEndCost()));

        return builder;
    }

    /**
     * 북마크 여부 조회 서브 쿼리
     *
     * @param lessonId 과외 식별자(NumberPath)
     * @param memberId 사용자 식별자
     * @return JPQLQuery(북마크 여부)
     * @author mozzi327
     */
    private JPQLQuery<Boolean> isBookmarked(NumberPath<Long> lessonId,
                                            Long memberId) {
        return JPAExpressions.select(
                        new CaseBuilder()
                                .when(bookmark.isNotNull()).then(true)
                                .otherwise(false)
                ).from(bookmark)
                .where(bookmark.lessonId.eq(lessonId).and(bookmark.memberId.eq(memberId)));
    }
}
