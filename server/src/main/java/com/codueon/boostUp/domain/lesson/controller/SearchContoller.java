package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.GetLessonDetail;
import com.codueon.boostUp.domain.lesson.dto.GetStudentLesson;
import com.codueon.boostUp.domain.lesson.dto.PostSearchLesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.domain.lesson.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchContoller {
    // TODO : Notice! 해당 서비스는 LessonController와 합쳐질 예정입니다.

    private final SearchService searchService;

    @GetMapping("/lesson")
    public ResponseEntity<?> getMyPageLesson() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩
        Page<GetStudentLesson> response = searchService.getMyLessons(memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity<?> getDetailLesson(@PathVariable("lesson-id") Long lessonId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/search")
    public ResponseEntity searchLesson(@RequestBody PostSearchLesson postSearchLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
