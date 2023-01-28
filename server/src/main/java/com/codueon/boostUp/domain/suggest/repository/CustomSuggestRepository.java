package com.codueon.boostUp.domain.suggest.repository;

import com.codueon.boostUp.domain.suggest.dto.GetPaymentInfo;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentReceipt;
import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetTutorSuggest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomSuggestRepository {
    Page<GetTutorSuggest> getTutorSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable);
    Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable);
    GetPaymentInfo getPaymentInfoOnMyPage(Long suggestId, Long memberId);
    GetPaymentReceipt getPaymentReceiptOnMyPage(Long suggestId, Long memberId);
}
