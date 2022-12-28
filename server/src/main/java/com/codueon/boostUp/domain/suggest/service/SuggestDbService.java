package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTeacherSuggest;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
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

    public Suggest ifExistsReturnSuggest(Long suggestId) {
        return suggestRepository.findById(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    public Suggest saveSuggest(Suggest suggest) {
        return suggestRepository.save(suggest);
    }

    public Page<GetTeacherSuggest> getTeacherSuggestsOnMyPage(Long lessonId, Long memberId, Long tabId, Pageable pageable) {
        return suggestRepository.getTeacherSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
    }

    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        return suggestRepository.getStudentSuggestsOnMyPage(memberId, pageable);
    }
}
