package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.global.file.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    private final LessonDbService lessonDbService;

    @PostMapping(value = "/lesson/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postLesson(@RequestPart(value = "data") PostLesson postLesson,
                                        @RequestPart(value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(value = "careerImage") List<MultipartFile> careerImage) throws Exception  {
        Long memberId = 1L;
        lessonService.createLesson(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/lesson")
    public ResponseEntity<?> getMypageLesson(@RequestBody GetStudentLesson getMypageLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/lesson/{lesson-id}")
    public ResponseEntity<?> getDetailLesson(@PathVariable("lesson-id") Long lessonId,
                                             @RequestBody GetLessonDetail getDetailLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/lesson/{lesson-id}/edit")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") PostEditLesson postEditLesson,
                                       @RequestPart(value = "profileImage") MultipartFile file) throws Exception {
        Long memberId = 1L;

        return null;
    }

    @DeleteMapping("/lesson/{lesson-id}")
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/search")
    public ResponseEntity searchLesson(@RequestBody PostSearchLesson postSearchLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
