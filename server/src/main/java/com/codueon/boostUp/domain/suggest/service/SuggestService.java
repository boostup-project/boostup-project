package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestService {

    private final SuggestDbService suggestDbService;
    private final LessonDbService lessonDbService;

    @Transactional
    public void createSuggest(PostSuggest post, Long lessonId, Long memberId) {
//        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

//        if(memberId.equals(findLesson.getMemberId())) {
//            throw new BusinessLogicException(ExceptionCode.TUTOR_CANNOT_RESERVATION);
//        }

        Suggest suggest = Suggest.builder()
                .days(post.getDays())
                .languages(post.getLanguages())
                .requests(post.getRequests())
                .build();

        suggestDbService.saveSuggest(suggest);
    }

    @Transactional
    public void acceptSuggest(Long lessonId, Long suggestId, Long memberId, Integer quantity) {

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

        if (!memberId.equals(findLesson.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        findSuggest.setQuantity(quantity);
        findSuggest.setStartTime();
        findSuggest.setStatus(Suggest.SuggestStatus.PAY_IN_PROGRESS);

        suggestDbService.saveSuggest(findSuggest);

    }


}
