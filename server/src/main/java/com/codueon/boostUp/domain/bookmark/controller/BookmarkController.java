package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import com.codueon.boostUp.domain.bookmark.dto.WrapBookmark;
import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * 사용자 북마크 여부 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return Boolean
     * @author mozzi327
     */
    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity getBookmark(@PathVariable("lesson-id") Long lessonId) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        boolean isBookmarked = bookmarkService.isMemberBookmarked(memberId, lessonId);
        return ResponseEntity.ok().body(new WrapBookmark(isBookmarked));
    }

    /**
     * 북마크 추가 / 삭제 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return isBookmark
     * @author mozzi327
     */
    @GetMapping("/lesson/{lesson-id}/edit")
    public ResponseEntity changeBookmark(@PathVariable("lesson-id") Long lessonId) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        boolean isBookmarked = bookmarkService.changeBookmarkStatus(memberId, lessonId);
        return ResponseEntity.ok().body(new WrapBookmark(isBookmarked));
    }

    /**
     * 마이페이지 북마크 리스트 조회 컨트롤러 메서드
     * @param pageable 페이지 정보
     * @author mozzi327
     */
    @GetMapping
    public ResponseEntity getBookmarks(Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        Page<GetBookmark> bookmarkList = bookmarkService.findBookmarkList(memberId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(bookmarkList));
    }
}
