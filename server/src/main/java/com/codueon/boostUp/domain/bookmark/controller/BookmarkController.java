package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity changeBookmarks(@PathVariable("lesson-id") Long lessonId) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        bookmarkService.changeBookmarkStatus(memberId, lessonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getBookmarks() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩 ㅋㅋ
        List<Bookmark> bookmarkList = bookmarkService.findBookmarkList(memberId);
        return ResponseEntity.ok().build();
    }
}
