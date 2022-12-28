package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
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

    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity changeBookmark(@PathVariable("lesson-id") Long lessonId) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        boolean isBookmarked = bookmarkService.changeBookmarkStatus(memberId, lessonId);
        return ResponseEntity.ok().body(isBookmarked);
    }

    @GetMapping
    public ResponseEntity getBookmarks(Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        Page<GetBookmark> bookmarkList = bookmarkService.findBookmarkList(memberId, pageable);
        return ResponseEntity.ok().build();
    }
}
