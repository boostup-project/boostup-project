package com.codueon.boostUp.domain.reveiw.repository;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.QGetReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.member.entity.QMember.member;
import static com.codueon.boostUp.domain.member.entity.QMemberImage.memberImage;
import static com.codueon.boostUp.domain.reveiw.entity.QReview.review;

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
}
