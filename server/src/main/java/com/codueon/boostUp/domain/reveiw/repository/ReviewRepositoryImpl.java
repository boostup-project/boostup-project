package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.LessonLanguage;
import com.codueon.boostUp.domain.lesson.entity.QLesson;
import com.codueon.boostUp.domain.lesson.entity.QLessonLanguage;
import com.codueon.boostUp.domain.member.entity.QMember;
import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import com.codueon.boostUp.domain.reveiw.dto.QGetReview;
import com.codueon.boostUp.domain.reveiw.dto.QGetReviewMyPage;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.DayOfWeek;
import java.util.List;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.lesson.entity.QLessonAddress.lessonAddress;
import static com.codueon.boostUp.domain.lesson.entity.QLessonLanguage.lessonLanguage;
import static com.codueon.boostUp.domain.lesson.entity.QProfileImage.profileImage;
import static com.codueon.boostUp.domain.member.entity.QMember.member;
import static com.codueon.boostUp.domain.member.entity.QMemberImage.memberImage;
import static com.codueon.boostUp.domain.reveiw.entity.QReview.review;
import static com.codueon.boostUp.domain.suggest.entity.QSuggest.suggest;

public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetReview> getReviewList(Long lessonId, Pageable pageable) {
        List<GetReview> result = queryFactory
                .select(new QGetReview(
                        review.id,
                        member.memberImage.filePath,
                        member.name,
                        review.score,
                        review.comment,
                        review.createdAt))
                .from(review)
                .leftJoin(member).on(review.memberId.eq(member.id))
                .where(review.lessonId.eq(lessonId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.id.desc())
                .fetch();
        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<GetReviewMyPage> getMyPageReviewList(Long memberId, Pageable pageable) {
        List<GetReviewMyPage> result = queryFactory
                .select(new QGetReviewMyPage(
                        review.lessonId,
                        member.name,
                        review.score,
                        review.comment,
                        review.createdAt,
                        lesson,
                        suggest.startTime,
                        suggest.endTime
                )).from(review, lesson)
                .leftJoin(lesson).on(lesson.id.eq(review.lessonId))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .leftJoin(suggest).on(suggest.memberId.eq(review.memberId))
                .where(review.memberId.eq(memberId),
                       member.id.eq(lesson.memberId),
                       suggest.memberId.eq(memberId))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.lessonId.desc())
                .fetch();

        long total2 = result.size();
        return new PageImpl<>(result, pageable, total2);
    }
}

