package com.codueon.boostUp.domain.bookmark.repository;

import java.util.List;
import javax.persistence.EntityManager;

import com.codueon.boostUp.domain.bookmark.dto.QGetBookmark;
import com.codueon.boostUp.domain.member.entity.Member;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.member.entity.QMember.member;

public class BookmarkRepositoryImpl implements CustomBookmarkRepository {

    private final JPAQueryFactory queryFactory;
    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetBookmark> getBookmarkList(Long memberId, Pageable pageable) {
        List<GetBookmark> result = queryFactory
                .select(new QGetBookmark(bookmark,
                        member.name,
                        lesson
                ))
                .from(bookmark)
                .leftJoin(member).on(bookmark.memberId.eq(member.id))
                .leftJoin(lesson).on(bookmark.lessonId.eq(lesson.id))
                .where(bookmark.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookmark.id.desc())
                .fetch();
        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }
}
