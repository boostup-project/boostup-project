package com.codueon.boostUp.domain.lesson.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonDbService {
     private final LessonRepository lessonRepository;

    public Lesson ifExistsReturnLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
    }

    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }
}
