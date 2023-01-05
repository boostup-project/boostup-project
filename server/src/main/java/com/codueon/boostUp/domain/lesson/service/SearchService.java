package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.dto.*;
import com.codueon.boostUp.domain.lesson.entity.Curriculum;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.LessonInfo;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final LessonDbService lessonDbService;
    private final LessonRepository lessonRepository;

    /**
     * 사용자가 신청한 과외 목록을 조회하는 메서드
     * @param memberId 사용자 식별자
     * @return Page(GetStudentLesson)
     * @author mozzi327
     *
     */
    public Page<GetStudentLesson> getMyLessons(Long memberId, Pageable pageable) {
        return lessonRepository.getMyPageLessonInfo(memberId, pageable);
    }

    /**
     * 메인페이지 조회 메서드
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(GetMainPageLesson)
     * @author mozzi327
     */
    public Page<GetMainPageLesson> getMainPageLessons(Long memberId, Pageable pageable) {
        if (memberId == null) return lessonRepository.getMainPageLessons(pageable);
        else return lessonRepository.getMainPageLessonsAndBookmarkInfo(memberId, pageable);
    }

    /**
     * 과외 상세페이지 요약 정보 조회 메서드
     * @param lessonId 과외 식별자
     * @return GetLesson
     * @author mozzi327
     */
    public GetLesson getDetailLesson(Long lessonId) {
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
        return GetLesson.builder()
                .lesson(findLesson)
                .build();
    }

    /**
     * 과외 상세페이지 상세 정보 조회 메서드
     * @param lessonId 과외 식별자
     * @return GetLessonInfo
     * @author mozzi327
     */
    public GetLessonInfo getDetailLessonInfo(Long lessonId) {
        LessonInfo lessonInfo = lessonDbService.ifExsitsReturnLessonInfo(lessonId);
        return GetLessonInfo.builder()
                .lessonInfo(lessonInfo)
                .build();
    }

    /**
     * 과외 상세페이지 커리큘럼 정보 조회 메서드
     * @param lessonId 과외 식별자
     * @return GetLessonCurriculum
     * @author mozzi327
     */
    public GetLessonCurriculum getDetailLessonCurriculum(Long lessonId) {
        Curriculum findCurriculum = lessonDbService.ifExsistsReturnCurriculum(lessonId);
        return GetLessonCurriculum.builder()
                .curriculum(findCurriculum.getCurriculum())
                .build();
    }
}
