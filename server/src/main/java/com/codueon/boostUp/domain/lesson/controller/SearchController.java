package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class SearchController {
    // TODO : Notice! 해당 서비스는 LessonController 와 합쳐질 예정입니다.
    private final SearchService searchService;

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
        Page<GetMainPageLesson> response = searchService.getMainPageLessons(memberId, pageable);
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
        Page<GetMainPageLesson> response = searchService.getDetailSearchLessons(memberId, postSearchLesson, pageable);
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
        Page<GetMainPageLesson> response = searchService.getMainPageLessonsAboutLanguage(memberId, languageId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }

    /**
     * 과외 요약 정보 조회 컨트롤러 메서드(선생님 자기 과외 정보)
     * @return GetLesson
     * @author mozzi327
     */
    @GetMapping("/tutor")
    public ResponseEntity<?> getMyPageMyClassInfo() {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩
        GetTutorLesson response = searchService.getMyLesson(memberId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 과외 상세 페이지 요약 정보 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author mozzi327
     */
    @GetMapping("/{lesson-id}")
    public ResponseEntity<?> getLesson(@PathVariable("lesson-id") Long lessonId) {
        GetLesson response = searchService.getDetailLesson(lessonId);
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
        GetLessonInfo response = searchService.getDetailLessonInfo(lessonId);
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
        GetLessonCurriculum response = searchService.getDetailLessonCurriculum(lessonId);
        return ResponseEntity.ok().body(response);
    }
}
