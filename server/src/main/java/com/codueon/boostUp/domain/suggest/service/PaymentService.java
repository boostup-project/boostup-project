package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.suggest.dto.GetRefundPayment;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.DURING_LESSON;
import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.REFUND_PAYMENT;
import static com.codueon.boostUp.global.exception.ExceptionCode.INVALID_ACCESS;
import static com.codueon.boostUp.global.exception.ExceptionCode.NOT_REFUND_PAYMENT;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final SuggestDbService suggestDbService;
    private final SuggestService suggestService;
    private final LessonDbService lessonDbService;
    private final KakaoPayService kakaoPayService;
    private final TossPayService tossPayService;

    /**
     * 결제 여부 확인 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  사용자 식별자
     * @return Boolean
     * @author LeeGoh
     */
    public Boolean getPaymentStatusCheck(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getMemberId().equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        return findSuggest.getSuggestStatus().equals(DURING_LESSON);
    }

    /**
     * 환불 메서드 Kakao/Toss
     *
     * @param suggestId 신청 식별자
     * @param memberId  회원 식별자
     * @return Message
     * @author LeeGoh
     */
    public Message refundPaymentKakaoOrToss(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        suggestService.suggestGetMemberIdAndStatusIsDuringLesson(findSuggest, memberId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        if (findPaymentInfo.getQuantity() == findPaymentInfo.getQuantityCount())
            throw new BusinessLogicException(INVALID_ACCESS);

        if (findSuggest.getPaymentMethod().equals("카카오페이"))
            return kakaoPayService.refundKakaoPayment(findSuggest, findPaymentInfo);

        return tossPayService.refundTossPayment(findSuggest, findPaymentInfo);
    }

    /**
     * 환불 단건 조회 메서드
     *
     * @param suggestId 신청 식별자
     * @param memberId  회원 식별자
     * @return GetRefundPayment
     * @author LeeGoh
     */
    public GetRefundPayment getRefundPaymentInfo(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getMemberId().equals(memberId))
            throw new BusinessLogicException(INVALID_ACCESS);

        if (!findSuggest.getSuggestStatus().equals(REFUND_PAYMENT))
            throw new BusinessLogicException(NOT_REFUND_PAYMENT);

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getId());
        String name = lessonDbService.getNameByLessonId(findSuggest.getLessonId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        return GetRefundPayment.builder()
                .suggest(findSuggest)
                .name(name)
                .lesson(findLesson)
                .paymentInfo(findPaymentInfo)
                .build();
    }
}
