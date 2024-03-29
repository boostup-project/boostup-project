package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.suggest.dto.GetPaymentInfo;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentReceipt;
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

import java.util.List;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.END_OF_LESSON;
import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.REFUND_PAYMENT;
import static com.codueon.boostUp.global.exception.ExceptionCode.INVALID_ACCESS;

@Service
@RequiredArgsConstructor
public class SuggestDbService {
    private final SuggestRepository suggestRepository;
    private final ReasonRepository reasonRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    /*--------------------------------------- DB Create 메서드 --------------------------------------*/

    /**
     * 신청 조회 메서드
     *
     * @param suggestId 신청 식별자
     * @return Suggest
     * @author LeeGoh
     */
    public Suggest ifExistsReturnSuggest(Long suggestId) {
        return suggestRepository.findById(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SUGGEST_NOT_FOUND));
    }

    /**
     * 결제 정보 조회 메서드
     *
     * @param suggestId 신청 식별자
     * @return PaymentInfo
     * @author LeeGoh
     */
    public PaymentInfo ifExistsReturnPaymentInfo(Long suggestId) {
        return paymentInfoRepository.findBySuggestId(suggestId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SUGGEST_NOT_FOUND));
    }

    /**
     * 과외 신청 정보 존재 여부 확인 메서드
     *
     * @param suggestId 과외 신청 식별자
     * @param memberId 사용자 식별자
     * @author Suggest
     * @author mozzi327
     */
    public Suggest ifNotExistSuggestThrowException(Long lessonId, Long suggestId, Long memberId) {
        return suggestRepository.findByIdAndLessonIdAndMemberId(suggestId, lessonId, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SUGGEST_NOT_FOUND));
    }

    /**
     * 중복 신청 방지 메서드(Status 확인)
     *
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     */
    public void ifUnfinishedSuggestExistsReturnException(Long lessonId, Long memberId) {
        List<Suggest> findSuggest = suggestRepository.findAllByLessonIdAndMemberId(lessonId, memberId);
        if (!findSuggest.isEmpty()) {
            for (Suggest suggest : findSuggest) {
                if (!(suggest.getSuggestStatus().equals(END_OF_LESSON) ||
                        suggest.getSuggestStatus().equals(REFUND_PAYMENT)))
                    throw new BusinessLogicException(ExceptionCode.SUGGEST_ALREADY_EXIST);
            }
        }
    }

    /**
     * 신청 저장 메서드
     *
     * @param suggest 신청 정보
     * @author LeeGoh
     */
    public void saveSuggest(Suggest suggest) {
        suggestRepository.save(suggest);
    }

    /**
     * 거절 사유 저장 메서드
     *
     * @param reason 거절 사유 정보
     * @author LeeGoh
     */
    public void saveReason(Reason reason) {
        reasonRepository.save(reason);
    }

    /**
     * 결제 정보 저장 메서드
     *
     * @param paymentInfo 결제 정보
     * @author LeeGoh
     */
    public void savePayment(PaymentInfo paymentInfo) {
        paymentInfoRepository.save(paymentInfo);
    }

    /*--------------------------------------- DB Read 메서드 --------------------------------------*/

    /**
     * 결제 페이지 요청 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @return GetPaymentInfo
     * @author LeeGoh
     */
    public GetPaymentInfo getPaymentInfoOnMyPage(Long suggestId, Long memberId) {
        return suggestRepository.getPaymentInfoOnMyPage(suggestId, memberId);
    }

    /**
     * 결제 영수증 요청 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @return GetPaymentReceipt
     * @author LeeGoh
     */
    public GetPaymentReceipt getPaymentReceiptOnMyPage(Long suggestId, Long memberId) {
        return suggestRepository.getPaymentReceiptOnMyPage(suggestId, memberId);
    }

    /**
     * 마이페이지 선생님용 신청 내역 조회 메서드
     *
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
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page
     * @author LeeGoh
     */
    public Page<GetStudentSuggest> getStudentSuggestsOnMyPage(Long memberId, Pageable pageable) {
        return suggestRepository.getStudentSuggestsOnMyPage(memberId, pageable);
    }

    /**
     * 과외에 대한 과외 신청 내역 전체 조회 메서드
     *
     * @param lessonId 과외 식별자
     * @return Lesson
     * @author Quartz614
     */
    public List<Suggest> findAllSuggestForLesson(Long lessonId) {
        return suggestRepository.findAllByLessonId(lessonId);
    }

    /**
     * 과외 신청한 사용자 닉네임 조회 메서드
     *
     * @param suggestId 과외 식별자
     * @return String
     * @author LeeGoh
     */
    public String findStudentNameBySuggestId(Long suggestId) {
        return suggestRepository.findStudentNameBySuggestId(suggestId);
    }

    /*--------------------------------------- DB Delete 메서드 --------------------------------------*/

    /**
     * 신청 삭제 메서드
     *
     * @param suggest 신청 정보
     * @author LeeGoh
     */
    public void deleteSuggest(Suggest suggest) {
        suggestRepository.delete(suggest);
    }

    /*--------------------------------------- 출석부 메서드 --------------------------------------*/

    /**
     * 출석 인정(출석 횟수 증가) 메서드
     *
     * @param paymentInfo 결제 정보
     * @return PaymentInfo
     * @author LeeGoh
     */
    public PaymentInfo checkQuantityCount(PaymentInfo paymentInfo) {
        if (paymentInfo.getQuantityCount() >= paymentInfo.getQuantity()) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }
        paymentInfo.addQuantityCount();
        return paymentInfoRepository.save(paymentInfo);
    }

    /**
     * 출석 인정 취소(출석 횟수 감소) 메서드
     *
     * @param paymentInfo
     * @return PaymentInfo
     * @author LeeGoh
     */
    public PaymentInfo cancelQuantityCount(PaymentInfo paymentInfo) {
        paymentInfo.reduceQuantityCount();
        if (paymentInfo.getQuantityCount() < 0) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }
        return paymentInfoRepository.save(paymentInfo);
    }
}
