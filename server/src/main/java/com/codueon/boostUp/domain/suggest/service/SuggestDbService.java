package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTutorSuggest;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Reason;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.repository.PaymentInfoRepository;
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
    private final PaymentInfoRepository paymentInfoRepository;

    public Suggest ifExistsReturnSuggest(Long suggestId) {
        return suggestRepository.findById(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    public void saveSuggest(Suggest suggest) {
        suggestRepository.save(suggest);
    }

    public void saveReason(Reason reason) {
        reasonRepository.save(reason);
    }

    public void savePayment(PaymentInfo paymentInfo) {
        paymentInfoRepository.save(paymentInfo);
    }

    public void deleteSuggest(Suggest suggest) {
        suggestRepository.delete(suggest);
    }

    public Page<GetTutorSuggest> getTutorSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable) {
        return suggestRepository.getTutorSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
    }

    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        return suggestRepository.getStudentSuggestsOnMyPage(memberId, pageable);
    }

}
