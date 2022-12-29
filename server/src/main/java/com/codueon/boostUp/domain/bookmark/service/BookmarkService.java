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

    /**
     * 북마크 여부 조회 메서드
     * @param memberId 사용자 식별자
     * @param lessonId 과외 식별자
     * @return isBookmark
     * @author mozzi327
     */
    public boolean isMemberBookmarked(Long memberId, Long lessonId) {
        boolean isBookmark = true;
        Optional<Bookmark> findBookmark = bookmarkRepository.findByMemberIdAndLessonId(memberId, lessonId);
        if (findBookmark.isEmpty()) isBookmark = false;
        return isBookmark;
    }

    /**
     * 북마크 상태 변경 메서드(추가/삭제)
     * @param memberId 사용자 식별자
     * @param lessonId 과외 식별자
     * @return isBookmark
     * @author mozzi327
     */
    public boolean changeBookmarkStatus(Long memberId, Long lessonId) {
        Optional<Bookmark> findBookmark = bookmarkRepository.findByMemberIdAndLessonId(memberId, lessonId);
        boolean isBookmark = false;
        if (findBookmark.isPresent()) bookmarkRepository.delete(findBookmark.get());
        else {
            bookmarkRepository.save(
                    Bookmark.builder()
                            .memberId(memberId)
                            .lessonId(lessonId)
                            .build()
            );
            isBookmark = true;
        }
        return isBookmark;
    }

    /**
     * 사용자 북마크 리스트 조회 메서드
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(GetBookmark)
     * @author mozzi327
     */
    public Page<GetBookmark> findBookmarkList(Long memberId, Pageable pageable) {
        return bookmarkRepository.getBookmarkList(memberId, pageable);
    }
}
