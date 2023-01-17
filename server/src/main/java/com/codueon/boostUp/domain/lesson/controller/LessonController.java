package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    /**
     * 과외 등록 컨트롤러
     * @param postLesson 과외 등록
     * @param profileImage 프로필 이미지
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @throws Exception
     * @author Quartz614
     */
    @PostMapping(value = "/registration")
    public ResponseEntity<?> postLesson(@RequestPart(value = "data") PostLesson postLesson,
                                        @RequestPart(value = "profileImage") MultipartFile profileImage,
                                        @RequestPart(value = "careerImage") List<MultipartFile> careerImage){

        Long memberId = 1L;

        /** 로컬 환경 */
        lessonService.createLesson(postLesson, memberId, profileImage, careerImage);

        /** S3 환경 */
//        lessonService.createLessonS3(postLesson, memberId, profileImage, careerImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 과외 요약 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param postLessonInfoEdit 수정 과외 요약 정보
     * @param profileImage 프로필 이미지
     * @return ResponseEntity
     * @author Quatz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/modification")
    public ResponseEntity updateLesson(@PathVariable("lesson-id") Long lessonId,
                                       @RequestPart(value = "data") PostLessonInfoEdit postLessonInfoEdit,
                                       @RequestPart(value = "profileImage") MultipartFile profileImage) {
        Long memberId = 1L;
        /** 로컬 환경 */
        lessonService.updateLessonInfo(lessonId, postLessonInfoEdit, memberId, profileImage);

        /** S3 환경 */
//        lessonService.updateLessonInfoS3(lessonId, postLessonInfoEdit, memberId, profileImage);
        return ResponseEntity.ok().build();
    }

    /**
     * 과외 상세 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param postLessonDetailEdit 수정 과외 상세 정보
     * @param careerImage 경력 이미지
     * @return ResponseEntity
     * @author Quartz614
     */
    @SneakyThrows
    @PostMapping("/{lesson-id}/detailInfo/modification")
    public ResponseEntity updateLessonDetail(@PathVariable("lesson-id") Long lessonId,
                                             @RequestPart(value = "data") PostLessonDetailEdit postLessonDetailEdit,
                                             @RequestPart(value = "careerImage") List<MultipartFile> careerImage) {
        Long memberId = 1L;
        /** 로컬 환경 */
        lessonService.updateLessonDetail(lessonId, postLessonDetailEdit, memberId, careerImage);

        /** S3 환경 */
//        lessonService.updateLessonDetailS3(lessonId, postLessonDetailEdit, memberId, careerImage);

        return ResponseEntity.ok().build();
    }

    /**
     * 과외 진행 방식 정보 수정 컨트롤러
     * @param lessonId 과외 식별자
     * @param patchLessonCurriculum 과외 진행 방식 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/{lesson-id}/curriculum/modification")
    public ResponseEntity updateCurriculum(@PathVariable("lesson-id") Long lessonId,
                                           @RequestBody PatchLessonCurriculum patchLessonCurriculum) {
        Long memberId = 1L;
        lessonService.updateCurriculum(lessonId, patchLessonCurriculum, memberId);

        return ResponseEntity.ok().build();
    }

    /**
     * 과외 삭제 컨트롤러
     * @param lessonId 과외 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @DeleteMapping(value = "/{lesson-id}")
    public ResponseEntity deleteLesson(@PathVariable("lesson-id") Long lessonId) {
        Long memberId = 1L;
        /** 로컬 환경 */
        lessonService.deleteLesson(memberId, lessonId);

        /** S3 환경 */
//        lessonService.deleteLessonS3(memberId, lessonId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 등록한 과외 마이페이지 조회 컨트롤러
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping(value = "/tutor")
    public ResponseEntity<String> getLessonMypage() {
        Long memberId = 1L;
        String response = lessonService.getLessonMypage(memberId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 메인페이지 과외 전체 조회 컨트롤러 메서드
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @GetMapping
    public ResponseEntity<?> getMainPageLessonInfos(Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L;
        Page<GetMainPageLesson> response = lessonService.getMainPageLessons(memberId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }
    /**
     * 메인페이지 상세 검색 컨트롤러 메서드
     * @param postSearchLesson 상세 검색 정보
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @PostMapping("/search")
    public ResponseEntity<?> getDetailSearchForLesson(@RequestBody PostSearchLesson postSearchLesson,
                                                      Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L;
        Page<GetMainPageLesson> response = lessonService.getDetailSearchLessons(memberId, postSearchLesson, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 메인페이지 언어 별 과외 조회 컨트롤러 메서드
     * @param languageId 사용 언어 식별자
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    @GetMapping("/language/{language-id}")
    public ResponseEntity<?> getLessonByLanguage(@PathVariable("language-id") Long languageId,
                                                 Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L;
        Page<GetMainPageLesson> response = lessonService.getMainPageLessonsAboutLanguage(memberId, languageId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 과외 상세 페이지 요약 정보 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}")
    public ResponseEntity<?> getLesson(@PathVariable("lesson-id") Long lessonId) {
        GetLesson response = lessonService.getDetailLesson(lessonId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 과외 상세 페이지 상세 정보 조회 컨트롤러 메서드
     * @param lessonId
     * @return GetLessonInfo
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}/detailInfo")
    public ResponseEntity<?> getLessonInfo(@PathVariable("lesson-id") Long lessonId) {
        GetLessonInfo response = lessonService.getDetailLessonInfo(lessonId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 과외 상세 페이지 커리큘럼 정보 조회 컨트롤러 메서드
     * @param lessonId
     * @return GetLessonCurriculum
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}/curriculum")
    public ResponseEntity<?> getCurriculum(@PathVariable("lesson-id") Long lessonId) {
        GetLessonCurriculum response = lessonService.getDetailLessonCurriculum(lessonId);
        return ResponseEntity.ok().body(response);
    }
}
