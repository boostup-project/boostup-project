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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestDbService {

    private final SuggestRepository suggestRepository;
    private final ReasonRepository reasonRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    /**
     * 신청 조회 메서드
     * @param suggestId 신청 식별자
     * @return Suggest
     * @author LeeGoh
     */
    public Suggest ifExistsReturnSuggest(Long suggestId) {
        return suggestRepository.findById(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    /**
     * 결제정보 조회 메서드
     * @param suggestId 신청 식별자
     * @return PaymentInfo
     * @author LeeGoh
     */
    public PaymentInfo ifExistsReturnPaymentInfo(Long suggestId) {
        return paymentInfoRepository.findBySuggestId(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }

    /**
     * 결제 정보 조회 메서드 2 - 예외처리 X
     * @param suggestId 신청 식별자
     * @return PaymentInfo
     * @author LeeGoh
     */
    public Optional<PaymentInfo> isPaymentInfo(Long suggestId) {
        return paymentInfoRepository.findBySuggestId(suggestId);
    }

    /**
     * 신청 저장 메서드
     * @param suggest 신청 정보
     * @author LeeGoh
     */
    public void saveSuggest(Suggest suggest) {
        suggestRepository.save(suggest);
    }

    /**
     * 거절 사유 저장 메서드
     * @param reason 거절 사유 정보
     * @author LeeGoh
     */
    public void saveReason(Reason reason) {
        reasonRepository.save(reason);
    }

    /**
     * 결제 정보 저장 메서드
     * @param paymentInfo 결제 정보
     * @author LeeGoh
     */
    public void savePayment(PaymentInfo paymentInfo) {
        paymentInfoRepository.save(paymentInfo);
    }

    /**
     * 신청 삭제 메서드
     * @param suggest 신청 정보
     * @author LeeGoh
     */
    public void deleteSuggest(Suggest suggest) {
        suggestRepository.delete(suggest);
    }

    /**
     * 결제 정보 삭제 메서드
     * @param paymentInfo 결제 정보
     * @author LeeGoh
     */
    public void deletePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoRepository.delete(paymentInfo);
    }

    /**
     * 마이페이지 선생님용 신청 내역 조회 메서드
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @param tabId 탭 번호
     * @param pageable 페이지 정보
     * @return Page
     * @author LeeGoh
     */
    public Page<GetTutorSuggest> getTutorSuggestsOnMyPage(Long lessonId, Long memberId, int tabId, Pageable pageable) {
        return suggestRepository.getTutorSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
    }

    /**
     * 마이페이지 학생용 신청 내역 조회 메서드
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page
     * @author LeeGoh
     */
    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        return suggestRepository.getStudentSuggestsOnMyPage(memberId, pageable);
    }

}
