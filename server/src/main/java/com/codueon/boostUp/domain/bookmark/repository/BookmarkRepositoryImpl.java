package com.codueon.boostUp.domain.bookmark.repository;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import com.codueon.boostUp.domain.bookmark.dto.QGetBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codueon.boostUp.domain.bookmark.entity.QBookmark.bookmark;
import static com.codueon.boostUp.domain.lesson.entity.QLesson.lesson;
import static com.codueon.boostUp.domain.member.entity.QMember.member;

@Repository
public class BookmarkRepositoryImpl implements CustomBookmarkRepository {
    private final JPAQueryFactory queryFactory;
    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 북마크 전체 조회(마이페이지)
     * @param memberId 회원 식별자
     * @param pageable 페이지네이션
     * @return PageImpl
     * @author Mozzi327
     */
    @Override
    public Page<GetBookmark> getBookmarkList(Long memberId, Pageable pageable) {
        List<GetBookmark> result = queryFactory
                .select(new QGetBookmark(
                        lesson,
                        bookmark.id,
                        bookmark.bookmarkUrl,
                        member.name
                ))
                .from(bookmark)
                .leftJoin(lesson).on(bookmark.lessonId.eq(lesson.id))
                .leftJoin(member).on(lesson.memberId.eq(member.id))
                .where(bookmark.memberId.eq(memberId))
                .orderBy(bookmark.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = result.size();
        return new PageImpl<>(result, pageable, total);
    }
}
