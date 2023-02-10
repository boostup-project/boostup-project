package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.dto.post.PostLesson;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.domain.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson/test")
public class LessonTestController {
    private final LessonService lessonService;

    /**
     * 과외 등록 테스트 컨트롤러 메서드
     *
     * @param postLesson   과외 등록
     * @param profileImage 프로필 이미지
     * @param careerImage  경력 이미지
     * @author Quartz614
     */
    @PostMapping(value = "/registration")
    public ResponseEntity<?> testPostLesson(@RequestPart(value = "data") @Valid PostLesson postLesson,
                                            Authentication authentication,
                                            @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
                                            @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage) {
        lessonService.createLesson(postLesson, AuthVO.ofMemberId(authentication), profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 요약 정보 수정 테스트 컨트롤러 메서드
     *
     * @param lessonId           과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약 정보
     * @param profileImage       프로필 이미지
     * @param authentication     인증 정보
     * @author Quatz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/modification")
    public ResponseEntity<?> testUpdateLesson(@PathVariable("lesson-id") Long lessonId,
                                           @RequestPart(value = "data") @Valid PostLessonInfoEdit postLessonInfoEdit,
                                           @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
                                           Authentication authentication) {
        lessonService.updateLessonInfo(lessonId, AuthVO.ofMemberId(authentication), postLessonInfoEdit, profileImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 상세 정보 수정 테스트 컨트롤러 메서드
     *
     * @param lessonId             과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세 정보
     * @param careerImage          경력 이미지
     * @param authentication       인증 정보
     * @author Quartz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/detailInfo/modification")
    public ResponseEntity<?> testUpdateLessonDetail(@PathVariable("lesson-id") Long lessonId,
                                                 @RequestPart(value = "data") @Valid PostLessonDetailEdit postLessonDetailEdit,
                                                 @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage,
                                                 Authentication authentication) {
        lessonService.updateLessonDetail(lessonId, AuthVO.ofMemberId(authentication), postLessonDetailEdit, careerImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 삭제 테스트 컨트롤러 메서드
     *
     * @param lessonId       과외 식별자
     * @param authentication 인증 정보
     * @author Quartz614
     */
    @DeleteMapping(value = "/{lesson-id}")
    public ResponseEntity<?> testDeleteLesson(@PathVariable("lesson-id") Long lessonId,
                                           Authentication authentication) {
        lessonService.deleteLesson(AuthVO.ofMemberId(authentication), lessonId);
        return ResponseEntity.noContent().build();
    }
}
