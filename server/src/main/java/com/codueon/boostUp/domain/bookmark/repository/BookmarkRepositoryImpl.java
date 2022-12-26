package com.codueon.boostUp.domain.bookmark.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;

public class BookmarkRepositoryImpl implements CustomBookmarkRepository {

    private final JPAQueryFactory queryFactory;
    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetBookmark> getBookmarkList(Long memberId, Pageable pageable) {
//        List<GetBookmark> result = queryFactory.select(new QGetBookmark)
        return null;
    }
}
