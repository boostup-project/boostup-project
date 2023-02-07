package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.member.entity.QMember.member;
import static com.codueon.boostUp.domain.reveiw.entity.QReview.review;
import static com.codueon.boostUp.domain.suggest.entity.QPaymentInfo.paymentInfo;
import static com.codueon.boostUp.domain.suggest.entity.QSuggest.suggest;
import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.*;

public class SuggestRepositoryImpl implements CustomSuggestRepository {
    private final JPAQueryFactory queryFactory;

    public SuggestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 마이페이지 선생님 신청 내역 조회 QueryDSL
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @param tabId 탭 번호
     * @param pageable 페이지 정보
     * @return Page
     * @author LeeGoh
     */
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
                .where(lesson.memberId.eq(memberId),
                        suggest.lessonId.eq(lessonId),
                        changeStatusByTabId(tabId))
                .orderBy(suggest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    /**
     * tabId별 조회 Where문 메서드
     * @param tabId 탭 번호
     * @return suggestStatus
     * @author LeeGoh
     */
    private BooleanExpression changeStatusByTabId(int tabId) {
        switch (tabId) {
            case 1:
                return suggest.suggestStatus.eq(ACCEPT_IN_PROGRESS).or(
                        suggest.suggestStatus.eq(PAY_IN_PROGRESS));
            case 2:
                return suggest.suggestStatus.eq(DURING_LESSON);
            default:
                return suggest.suggestStatus.eq(END_OF_LESSON).or(
                        suggest.suggestStatus.eq(REFUND_PAYMENT));
        }
    }

    /**
     * 마이페이지 학생 신청 내역 조회 QueryDSL
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page
     * @author LeeGoh
     */
    @Override
    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        List<GetStudentSuggest> results = queryFactory
                .select(new QGetStudentSuggest(
                        suggest.id,
                        member.name,
                        suggest.suggestStatus,
                        suggest.startTime,
                        suggest.endTime,
                        lesson,
                        isReviewed(suggest.id, memberId)
                ))
                .from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .distinct()
                .where(suggest.memberId.eq(memberId))
                .orderBy(suggest.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    private JPQLQuery<Boolean> isReviewed(NumberPath<Long> suggestId,
                                          Long memberId) {
        return JPAExpressions.select(
                        new CaseBuilder()
                                .when(review.isNotNull()).then(true)
                                .otherwise(false).as("reviewCheck")
                ).from(review)
                .where(review.suggestId.eq(suggestId).and(review.memberId.eq(memberId)));
    }

    /**
     * 결제 상세 정보 조회 QueryDSL
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @return GetPaymentInfo
     * @author LeeGoh
     */
    public GetPaymentInfo getPaymentInfoOnMyPage(Long suggestId, Long memberId) {
        GetPaymentInfo result = queryFactory
                .select(new QGetPaymentInfo(
                        lesson,
                        member.name,
                        suggest.totalCost,
                        paymentInfo.quantity
                )).from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .leftJoin(paymentInfo).on(suggest.id.eq(paymentInfo.suggest.id))
                .where(suggest.memberId.eq(memberId).and(suggest.id.eq(suggestId)))
                .fetchOne();
        return result;
    }

    /**
     * 결제 영수증 조회 QueryDSL
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @return GetPaymentReceipt
     * @author LeeGoh
     */
    public GetPaymentReceipt getPaymentReceiptOnMyPage(Long suggestId, Long memberId) {
        GetPaymentReceipt result = queryFactory
                .select(new QGetPaymentReceipt(
                        lesson,
                        member.name,
                        suggest.totalCost,
                        paymentInfo.quantity,
                        suggest.paymentMethod
                )).from(suggest)
                .leftJoin(lesson).on(suggest.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .leftJoin(paymentInfo).on(suggest.id.eq(paymentInfo.suggest.id))
                .where(suggest.memberId.eq(memberId).and(suggest.id.eq(suggestId)))
                .fetchOne();
        return result;
    }
}
