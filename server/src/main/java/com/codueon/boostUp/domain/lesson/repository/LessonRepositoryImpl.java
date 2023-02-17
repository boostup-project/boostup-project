package com.codueon.boostUp.domain.lesson.repository;

import com.codueon.boostUp.domain.lesson.dto.get.*;
import com.codueon.boostUp.domain.lesson.dto.post.PostSearchLesson;
import com.codueon.boostUp.domain.lesson.entity.AddressInfo;
import com.codueon.boostUp.domain.lesson.entity.LanguageInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.lesson.entity.QLessonAddress.lessonAddress;
import static com.codueon.boostUp.domain.lesson.entity.QLessonLanguage.lessonLanguage;
import static com.codueon.boostUp.domain.member.entity.QMember.member;

@Repository
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
                .from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .distinct()
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
                .from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .distinct()
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
                )).from(lessonLanguage)
                .leftJoin(member).on(lessonLanguage.lesson.memberId.eq(member.id))
                .leftJoin(lesson).on(lessonLanguage.lesson.id.eq(lesson.id))
                .distinct()
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
                )).from(lessonLanguage)
                .leftJoin(member).on(lessonLanguage.lesson.memberId.eq(member.id))
                .leftJoin(lesson).on(lessonLanguage.lesson.id.eq(lesson.id))
                .distinct()
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
                )).from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .leftJoin(lessonAddress).on(lesson.id.eq(lessonAddress.lesson.id))
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .distinct()
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
                )).from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .leftJoin(lessonAddress).on(lesson.id.eq(lessonAddress.lesson.id))
                .leftJoin(lessonLanguage).on(lesson.id.eq(lessonLanguage.lesson.id))
                .distinct()
                .where(makeDetailSearchConditions(postSearchLesson))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lesson.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    /**
     * 과외 요약 정보 조회 (상세페이지) - 로그인 x
     *
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author Qruatz614
     */
    public GetLesson getDetailLesson(Long lessonId) {
        return queryFactory
                .select(new QGetLesson(
                        lesson,
                        member.name,
                        Expressions.constant(false)
                )).from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(lesson.id.eq(lessonId))
                .fetchOne();
    }

    /**
     * 과외 요약 정보 조회 (상세페이지) - 로그인 o
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @return GetLesson
     * @author mozzi327
     */
    @Override
    public GetLesson getDetailLessonForEditable(Long lessonId, Long memberId) {
        return queryFactory
                .select(new QGetLesson(
                        lesson,
                        member.name,
                        new CaseBuilder()
                                .when(lesson.memberId.eq(memberId)).then(true)
                                .otherwise(false)
                )).from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(lesson.id.eq(lessonId))
                .fetchOne();
    }

    /**
     * 알람 전송을 위한 선생님 정보 조회 QueryDSL
     *
     * @param lessonId 과외 식별자
     * @return GetLessonInfoForAlarm
     * @author mozzi327
     */
    @Override
    public GetLessonInfoForAlarm getLessonInfoForAlarm(Long lessonId) {
        return queryFactory
                .select(new QGetLessonInfoForAlarm(
                        member.id,
                        member.name,
                        lesson.title
                )).from(lesson)
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(lesson.id.eq(lessonId))
                .fetchOne();
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
        addAddressCondition(builder, postSearchLesson.getAddress());
        addLanguageCondition(builder, postSearchLesson.getLanguage());
        addTutorNameCondition(builder, postSearchLesson.getName());
        addTutorCareerCondition(builder, postSearchLesson.getCareer());
        addLessonCostCondition(builder, postSearchLesson.getStartCost(), postSearchLesson.getEndCost());
        return builder;
    }

    /**
     * 가능 지역 조건 메서드
     *
     * @param builder BooleanBuilder
     * @param address 가능 지역
     * @author mozzi327
     */
    private void addAddressCondition(BooleanBuilder builder, Integer address) {
        if (address == null) return;
        builder.and(lessonAddress.addressInfo.eq(AddressInfo.findById(address)));
    }

    /**
     * 사용 언어 조건 메서드
     *
     * @param builder  BooleanBuilder
     * @param language 사용 언어
     * @author mozzi327
     */
    private void addLanguageCondition(BooleanBuilder builder, Integer language) {
        if (language == null) return;
        builder.and(lessonLanguage.languageInfo.eq(LanguageInfo.findById(language)));
    }

    /**
     * 선생님 이름 조건 메서드
     *
     * @param builder BooleanBuilder
     * @param name    닉네임
     * @author mozzi327
     */
    private void addTutorNameCondition(BooleanBuilder builder, String name) {
        if (name == null) return;
        builder.and(member.name.contains(name));
    }

    /**
     * 경력 조건 메서드
     *
     * @param builder BooleanBuilder
     * @param career  경력
     * @author mozzi327
     */
    private void addTutorCareerCondition(BooleanBuilder builder, Integer career) {
        if (career == null) return;
        builder.and(lesson.career.goe(career));
    }

    /**
     * 과외 비용 조건 메서드
     *
     * @param builder   BooleanBuilder
     * @param startCost 금액 이상
     * @param endCost   금액 이하
     * @author mozzi327
     */
    private void addLessonCostCondition(BooleanBuilder builder, Integer startCost, Integer endCost) {
        if (startCost != null && endCost != null) builder.and(lesson.cost.between(startCost, endCost));
        else if (startCost != null) builder.and(lesson.cost.goe(startCost));
        else if (endCost != null) builder.and(lesson.cost.loe(endCost));
    }

    /**
     * 북마크 여부 조회 서브 QueryDsl
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

    /**
     * 과외 등록한 사용자 식별자 조회 QueryDsl
     *
     * @param lessonId 과외 식별자
     * @return Long
     * @author LeeGoh
     */
    public Long getMemberIdByLessonId(Long lessonId) {
        return queryFactory
                .select(member.id)
                .from(member)
                .leftJoin(lesson).on(member.id.eq(lesson.memberId))
                .where(lesson.id.eq(lessonId))
                .fetchOne();
    }

    /**
     * 과외 등록한 사용자 닉네임 조회 QueryDsl
     *
     * @param lessonId 과외 식별자
     * @return String
     * @author LeeGoh
     */
    public String getNameByLessonId(Long lessonId) {
        return queryFactory
                .select(member.name)
                .from(member)
                .leftJoin(lesson).on(member.id.eq(lesson.memberId))
                .where(lesson.id.eq(lessonId))
                .fetchOne();
    }
}
