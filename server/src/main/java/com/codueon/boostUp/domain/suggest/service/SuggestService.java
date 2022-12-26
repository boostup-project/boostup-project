package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestService {

    private final SuggestDbService suggestDbService;
//    private final LessonDbService lessonDbService;

    @Transactional
    public void createSuggest(PostSuggest post, Long lessonId, Long memberId) {
//        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
    }

}
