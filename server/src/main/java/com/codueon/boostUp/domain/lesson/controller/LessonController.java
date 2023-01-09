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

    @PostMapping(value = "/lesson/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postLesson(@RequestPart(value = "data") PostLesson postLesson,
                                        @RequestPart(value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(value = "careerImage") List<MultipartFile> careerImage) throws Exception  {
        Long memberId = 1L;
        lessonService.createLesson(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/lesson/{lesson-id}/edit")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") PostLessonInfoEdit postLessonInfoEdit,
                                       @RequestPart(value = "profileImage") MultipartFile profileImage) throws Exception {
        Long memberId = 1L;
        lessonService.updateLesson(lessonId, postLessonInfoEdit, memberId, profileImage);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lesson/{lesson-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        return ResponseEntity.noContent().build();
    }
}
