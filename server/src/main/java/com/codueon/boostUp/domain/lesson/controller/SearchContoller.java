package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SearchContoller {
    // TODO : Notice! 해당 서비스는 LessonController와 합쳐질 예정입니다.
    private final SearchService searchService;

    @GetMapping("/lesson")
    public ResponseEntity<?> getMyPageLesson(Pageable pageable) {
        // TODO : 시큐리티 적용 시 Authentication 객체 추가 요
        Long memberId = 1L; // 임시 하드 코딩
        Page<GetStudentLesson> response = searchService.getMyLessons(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 과외 상세 페이지 요약 정보 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author mozzi327
     */
    @GetMapping("/lesson/{lesson-id}")
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
    @GetMapping("/lesson/{lesson-id}/detailInfo")
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
    @GetMapping("/lesson/{lesson-id}/curriculum")
    public ResponseEntity<?> getCurriculum(@PathVariable("lesson-id") Long lessonId) {
        GetLessonCurriculum response = searchService.getDetailLessonCurriculum(lessonId);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/search")
    public ResponseEntity searchLesson(@RequestBody PostSearchLesson postSearchLesson) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/home")
    public ResponseEntity<MultiResponseDto<?>> getMainPageLessonInfos(Pageable pageable) {
        Long memberId = 1L;
        Page<GetMainPageLesson> response = searchService.getMainPageLessons(memberId, pageable);
        return ResponseEntity.ok().body(new MultiResponseDto<>(response));
    }
}
