package com.codueon.boostUp.domain.bookmark.service;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public boolean changeBookmarkStatus(Long memberId, Long lessonId) {
        Optional<Bookmark> findBookmark = bookmarkRepository.findByMemberIdAndLessonId(memberId, lessonId);
        boolean isBookmarked = false;
        if (findBookmark.isPresent()) bookmarkRepository.delete(findBookmark.get());
        else {
            bookmarkRepository.save(
                    Bookmark.builder()
                            .memberId(memberId)
                            .lessonId(lessonId)
                            .build()
            );
            isBookmarked = true;
        }
        return isBookmarked;
    }

    public Page<GetBookmark> findBookmarkList(Long memberId, Pageable pageable) {
        return bookmarkRepository.getBookmarkList(memberId, pageable);
    }
}
