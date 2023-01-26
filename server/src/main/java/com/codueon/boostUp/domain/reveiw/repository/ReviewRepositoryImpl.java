package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import com.codueon.boostUp.domain.reveiw.dto.QGetReview;
import com.codueon.boostUp.domain.reveiw.dto.QGetReviewMyPage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
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
                        review,
                        memberImage.filePath,
                        member.name
                ))
                .from(review)
                .leftJoin(member).on(review.memberId.eq(member.id))
                .leftJoin(memberImage).on(review.memberId.eq(memberImage.member.id))
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
                        review,
                        lesson,
                        member.name,
                        suggest.startTime,
                        suggest.endTime
                ))
                .from(review)
                .leftJoin(lesson).on(review.lessonId.eq(lesson.id))
                .leftJoin(suggest).on(review.memberId.eq(suggest.memberId))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(review.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.id.desc())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }
}
