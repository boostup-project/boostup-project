package com.codueon.boostUp.domain.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity changeBookmarkStatus(@PathVariable("lesson-id") Long lessonId) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getBookmarks() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        return ResponseEntity.ok().build();
    }
}
