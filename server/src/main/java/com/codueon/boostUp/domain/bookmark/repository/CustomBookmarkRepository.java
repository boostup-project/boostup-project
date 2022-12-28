package com.codueon.boostUp.domain.bookmark.repository;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookmarkRepository {
    Page<GetBookmark> getBookmarkList(Long memberId, Pageable pageable);
}
