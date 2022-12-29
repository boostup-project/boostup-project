package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTeacherSuggest;
import com.codueon.boostUp.domain.suggest.dto.PostReason;
import com.codueon.boostUp.domain.suggest.entity.Reason;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.repository.ReasonRepository;
import com.codueon.boostUp.domain.suggest.repository.SuggestRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuggestDbService {

    private final SuggestRepository suggestRepository;
    private final ReasonRepository reasonRepository;
    private final LessonDbService lessonDbService;

    public Suggest ifExistsReturnSuggest(Long suggestId) {
        return suggestRepository.findById(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    public Suggest saveSuggest(Suggest suggest) {
        return suggestRepository.save(suggest);
    }

    public Page<GetTeacherSuggest> getTeacherSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable) {
        return suggestRepository.getTeacherSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
    }

    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        return suggestRepository.getStudentSuggestsOnMyPage(memberId, pageable);
    }

    public void cancelSuggest(Long suggestId, Long memberId) {

        Suggest findSuggest = ifExistsReturnSuggest(suggestId);

        if (!memberId.equals(findSuggest.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        if (!findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS) &&
            !findSuggest.getStatus().equals(Suggest.SuggestStatus.PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        suggestRepository.delete(findSuggest);
    }

    public void declineSuggest(Long lessonId, Long suggestId, Long memberId, PostReason postReason) {

        Suggest findSuggest = ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

        if (!memberId.equals(findLesson.getMemberId()) ||
            !findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        Reason reason = Reason.builder().reason(postReason.getReason()).build();
        reasonRepository.save(reason);

        suggestRepository.delete(findSuggest);

    }
}
