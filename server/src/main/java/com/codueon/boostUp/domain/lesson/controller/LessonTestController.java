package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.Post.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.Patch.PostLessonDetailEdit;
import com.codueon.boostUp.domain.lesson.dto.Patch.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson/test")
public class LessonTestController {
    private final LessonService lessonService;

    /**
     * 과외 등록 테스트 컨트롤러 메서드
     * @param postLesson 과외 등록
     * @param profileImage 프로필 이미지
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @throws Exception
     * @author Quartz614
     */
    @PostMapping(value = "/registration")
    public ResponseEntity<?> testPostLesson(@RequestPart(value = "data") PostLesson postLesson,
                                        Authentication authentication,
                                        @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        lessonService.createLesson(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 요약 정보 수정 테스트 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약 정보
     * @param profileImage 프로필 이미지
     * @return ResponseEntity
     * @author Quatz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/modification")
    public ResponseEntity testUpdateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") PostLessonInfoEdit postLessonInfoEdit,
                                       Authentication authentication,
                                       @RequestPart(required = false, value = "profileImage") MultipartFile profileImage) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        lessonService.updateLessonInfo(lessonId, postLessonInfoEdit, profileImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 상세 정보 수정 테스트 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세 정보
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @author Quartz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/detailInfo/modification")
    public ResponseEntity testUpdateLessonDetail(@PathVariable("lesson-id") Long lessonId,
                                             @RequestPart(value = "data") PostLessonDetailEdit postLessonDetailEdit,
                                             @RequestPart(required = false, value = "careerImage") List<MultipartFile> careerImage) {
        lessonService.updateLessonDetail(lessonId, postLessonDetailEdit, careerImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 삭제 테스트 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @DeleteMapping(value = "/{lesson-id}")
    public ResponseEntity testDeleteLesson(@PathVariable("lesson-id") Long lessonId,
                                       Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        lessonService.deleteLesson(memberId, lessonId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그인 확인 메서드
     * @param token 토큰 정보
     * @return Long
     * @author mozzi327
     */
    private Long getMemberIdIfExistToken(JwtAuthenticationToken token) {
        if (token == null) return null;
        else return token.getId();
    }
}
