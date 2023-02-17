package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.chat.utils.AlarmType;
import com.codueon.boostUp.domain.lesson.dto.get.GetLessonInfoForAlarm;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.toss.*;
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
public class TossPayService {
    private final FeignService feignService;
    private final LessonDbService lessonDbService;
    private final SuggestDbService suggestDbService;
    private final SuggestEventService suggestEventService;

    @Value("${pay.request-url}")
    private String requestUrl;

    /**
     * Toss 결제 URL 요청 메서드
     *
     * @param suggestId 신청 식별자
     * @param paymentId 결제 방법
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getTossPayUrl(Long suggestId, int paymentId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        String method = "";
        switch (paymentId) {
            case 2:
                method = "휴대폰";
                break;
            case 3:
                method = "계좌이체";
                break;
            default:
                method = "카드";
        }

        TossPayHeader headers = feignService.setTossHeaders();
        ReadyToTossPayInfo body =
                feignService.setReadyTossParams(requestUrl, suggestId, findLesson.getCost(),
                        findLesson.getTitle(), findPaymentInfo.getQuantity(), method);

        TossPayReadyInfo tossPayReadyInfo = feignService.getTossPayReadyInfo(headers, body);

        findPaymentInfo.setTossPaymentInfo(body);
        findPaymentInfo.setPaymentKey(tossPayReadyInfo.getPaymentKey());
        suggestDbService.savePayment(findPaymentInfo);

        return Message.builder()
                .data(tossPayReadyInfo.getCheckout().getUrl())
                .message(TOSS_PAY_URI_MSG)
                .build();
    }

    /**
     * Toss 결제 성공 시 예약 정보 반환 메서드
     *
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getSuccessTossPaymentInfo(Long suggestId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        TossPayHeader headers = feignService.setTossHeaders();
        RequestForTossPayInfo body = feignService.setRequestBody(
                findPaymentInfo.getPaymentKey(), findPaymentInfo.getAmount(), findPaymentInfo.getOrderId());

        TossPaySuccessInfo tossPaySuccessInfo = feignService.getSuccessTossResponse(headers, body);

        switch (tossPaySuccessInfo.getMethod()) {
            case "휴대폰":
                findSuggest.setPaymentMethod("토스페이 휴대폰");
                break;
            case "계좌이체":
                findSuggest.setPaymentMethod("토스페이 계좌이체");
                break;
            default:
                findSuggest.setPaymentMethod("토스페이 카드");
        }

        tossPaySuccessInfo.setOrderStatus(ORDER_APPROVED);
        findSuggest.setStatus(DURING_LESSON);
        suggestDbService.saveSuggest(findSuggest);

        String studentName = suggestDbService.findStudentNameBySuggestId(suggestId);
        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(findSuggest.getLessonId());
        suggestEventService.sendAlarmMessage(tutorInfo.getTutorId(), tutorInfo.getTitle(),
                studentName, null, null, AlarmType.PAYMENT_SUCCESS);

        return Message.builder()
                .data(tossPaySuccessInfo)
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
     * Toss 환불 메서드
     *
     * @param suggest           신청 정보
     * @param quantity          과외 횟수
     * @param quantityCount     과외 진행 횟수
     * @param amount            과외 금액
     * @param paymentKey        paymentKey
     * @author LeeGoh
     */
    public Message refundTossPayment(Suggest suggest,
                                     Integer quantity,
                                     Integer quantityCount,
                                     Integer amount,
                                     String paymentKey) {
        TossPayHeader headers = feignService.setTossHeaders();
        RequestForTossPayCancelInfo body = feignService.setCancelBody(quantity, quantityCount, amount);

        TossPayCancelInfo cancelInfo =
                feignService.getCancelTossPaymentResponse(headers, paymentKey, body);

        cancelInfo.setOrderStatus(REFUND_APPROVED);
        // suggest.setStatus(REFUND_PAYMENT);
        suggest.setEndTime();
        suggestDbService.saveSuggest(suggest);

        GetLessonInfoForAlarm tutorInfo = lessonDbService.getLessonInfoForAlarm(suggest.getLessonId());
        suggestEventService.sendAlarmMessage(suggest.getMemberId(), tutorInfo.getTitle(),
                null, null, null, AlarmType.ACCEPT_REFUND);

        return Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();
    }
}
