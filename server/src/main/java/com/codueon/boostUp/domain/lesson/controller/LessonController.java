package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.GetDetailLesson;
import com.codueon.boostUp.domain.lesson.dto.GetMypageLesson;
import com.codueon.boostUp.domain.lesson.dto.PostEditLesson;
import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LessonController {
    @PostMapping("/lesson/regist")
    public ResponseEntity<?> postLesson(@RequestBody PostLesson postLesson) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/lesson")
    public ResponseEntity<?> getMypageLesson(@RequestBody GetMypageLesson getMypageLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity<?> getDetailLesson(@PathVariable("lesson-id") Long lessonId,
                                             @RequestBody GetDetailLesson getDetailLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/lesson/{lesson-id}/edit")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestBody PostEditLesson postEditLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/lesson/{lesson-id}")
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        return ResponseEntity.noContent().build();
    }
}