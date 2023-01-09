package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.global.file.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    /**
     * 과외 등록 컨트롤러
     *
     * @param postLesson 과외 등록
     * @param profileImage 프로필 이미지
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @throws Exception
     * @author Quartz614
     */
    @PostMapping(value = "/lesson/regist")
    public ResponseEntity<?> postLesson(@RequestPart(value = "data") PostLesson postLesson,
                                        @RequestPart(value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(value = "careerImage") List<MultipartFile> careerImage) throws Exception {
        Long memberId = 1L;
        lessonService.createLesson(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 요약 정보 수정 컨트롤러
     *
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약 정보
     * @param profileImage 프로필 이미지
     * @return ResponseEntity
     * @author Quatz614
     */
    @SneakyThrows
    @PostMapping("/lesson/{lesson-id}/edit")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") PostLessonInfoEdit postLessonInfoEdit,
                                       @RequestPart(value = "profileImage") MultipartFile profileImage) {
        Long memberId = 1L;
        lessonService.updateLessonInfo(lessonId, postLessonInfoEdit, memberId, profileImage);

        return ResponseEntity.ok().build();
    }

    /**
     * 과외 상세 정보 수정 컨트롤러
     *
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세 정보
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @author Quartz614
     */
    @SneakyThrows
    @PostMapping("/lesson/{lesson-id}/detailInfo/edit")
    public ResponseEntity updateLessonDetail(@PathVariable("lesson-id") Long lessonId,
                                             @RequestPart(value = "data") PostLessonDetailEdit postLessonDetailEdit,
                                             @RequestPart(value = "careerImage") List<MultipartFile> careerImage) {
        Long memberId = 1L;
        lessonService.updateLessonDetail(lessonId, postLessonDetailEdit, memberId, careerImage);

        return ResponseEntity.ok().build();
    }

    /**
     * 과외 진행 방식 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param patchLessonCurriculum 과외 진행 방식 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/lesson/{lesson-id}/curriculum/edit")
    public ResponseEntity updateCurriculum(@PathVariable("lesson-id") Long lessonId,
                                                 @RequestBody PatchLessonCurriculum patchLessonCurriculum) {
        Long memberId = 1L;
        lessonService.updateCurriculum(lessonId, patchLessonCurriculum, memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/lesson/{lesson-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        return ResponseEntity.noContent().build();
    }
}
