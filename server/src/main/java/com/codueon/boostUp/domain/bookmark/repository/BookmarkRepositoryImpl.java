package com.codueon.boostUp.domain.bookmark.repository;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import com.codueon.boostUp.domain.bookmark.dto.QGetBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

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
                .select(new QGetBookmark(
                        bookmark,
                        lesson,
                        member.name
                ))
                .from(bookmark)
                .leftJoin(lesson).on(bookmark.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(bookmark.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookmark.id.desc())
                .fetch();
        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }
}
