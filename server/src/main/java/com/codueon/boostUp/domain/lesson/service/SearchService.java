package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.dto.GetStudentLesson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final LessonDbService lessonDbService;

    /**
     * 사용자가 신청한 과외 목록을 조회하는 메서드
     * @param memberId 사용자 식별자
     * @return Page(GetStudentLesson)
     * @author mozzi327
     *
     */
    public Page<GetStudentLesson> getMyLessons(Long memberId) {

        return null;
    }
}
