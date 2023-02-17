package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfoForAlarm;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.kakao.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.DURING_LESSON;
import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.PAY_IN_PROGRESS;
import static com.codueon.boostUp.domain.suggest.utils.PayConstants.ORDER_APPROVED;
import static com.codueon.boostUp.domain.suggest.utils.PayConstants.REFUND_APPROVED;
import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static com.codueon.boostUp.global.exception.ExceptionCode.INVALID_ACCESS;

@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final FeignService feignService;
    private final LessonDbService lessonDbService;
    private final SuggestDbService suggestDbService;
    private final SuggestEventService suggestEventService;

    @Value("${pay.request-url}")
    private String requestUrl;

    /**
     * Kakao 결제 URL 요청 메서드
     *
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getKaKaoPayUrl(Long suggestId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        KakaoPayHeader headers = feignService.setKakaoHeaders();
        ReadyToKakaoPayInfo params = feignService.setReadyParams(
                requestUrl, suggestId, findSuggest.getTotalCost(), findSuggest.getMemberId(),
                findLesson.getTitle(), findLesson.getCost(), findPaymentInfo.getQuantity());

        KakaoPayReadyInfo payReadyInfo = feignService.getPayReadyInfo(headers, params);

        findPaymentInfo.setKakaoPaymentInfo(params, payReadyInfo.getTid());
        suggestDbService.savePayment(findPaymentInfo);

        return Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(KAKAO_PAY_URI_MSG)
                .build();
    }

    /**
     * Kakao 결제 성공 시 예약 정보 반환 메서드
     *
     * @param suggestId 신청 식별자
     * @param pgToken   Payment Gateway Token
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getSuccessKakaoPaymentInfo(Long suggestId, String pgToken) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        KakaoPayHeader headers = feignService.setKakaoHeaders();
        RequestForKakaoPayInfo params = feignService.setRequestParams(pgToken, findPaymentInfo);

        KakaoPaySuccessInfo kakaoPaySuccessInfo = feignService.getSuccessKakaoResponse(headers, params);

        kakaoPaySuccessInfo.setOrderStatus(ORDER_APPROVED);
        findSuggest.setPaymentMethod("카카오페이");
        findSuggest.setStatus(DURING_LESSON);
        suggestDbService.saveSuggest(findSuggest);

        String studentName = suggestDbService.findStudentNameBySuggestId(suggestId);
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(tutorInfo.getTutorId(), tutorInfo.getTitle(),
                studentName, null, null, AlarmType.PAYMENT_SUCCESS);

        return Message.builder()
                .data(kakaoPaySuccessInfo)
                .message(INFO_URI_MSG)
                .build();
    }

    /**
     * URL 요청 시 리턴되는 URL이 없을 때 발생하는 이벤트 메서드
     *
     * @return Message
     * @author LeeGoh
     */
    public Message<?> getFailedPayMessage() {
        return Message.builder()
                .message(FAILED_INFO_MESSAGE + "<br>" + INVALID_PARAMS)
                .build();
    }

    /**
     * Kakao 환불 메서드
     *
     * @param suggest       신청 정보
     * @param quantity      과외 횟수
     * @param quantityCount 과외 진행 횟수
     * @param totalAmount   총 환불 가격
     * @param tid           tid
     * @param cid           cid
     * @author LeeGoh
     */
    public Message refundKakaoPayment(Suggest suggest,
                                      Integer quantity,
                                      Integer quantityCount,
                                      Integer totalAmount,
                                      String tid,
                                      String cid) {
        KakaoPayHeader headers = feignService.setKakaoHeaders();
        RequestForKakaoPayCancelInfo params =
                feignService.setRequestCancelParams(quantity, quantityCount, totalAmount, tid, cid);

        KakaoPayCancelInfo cancelInfo =
                feignService.getCancelKakaoPaymentResponse(headers, params);

        cancelInfo.setOrderStatus(REFUND_APPROVED);
        // suggest.setStatus(REFUND_PAYMENT);
        suggest.setEndTime();
        suggestDbService.saveSuggest(suggest);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(suggest.getLessonId());
        suggestEventService.sendAlarmMessage(suggest.getMemberId(), tutorInfo.getTitle(), null, null, null, AlarmType.ACCEPT_REFUND);

        return Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();
    }

}
