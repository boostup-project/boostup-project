package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.suggest.dto.*;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Reason;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.kakao.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.toss.*;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.*;
import static com.codueon.boostUp.domain.suggest.utils.PayConstants.ORDER_APPROVED;
import static com.codueon.boostUp.domain.suggest.utils.PayConstants.REFUND_APPROVED;
import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static com.codueon.boostUp.global.exception.ExceptionCode.INVALID_ACCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestService {
    private final SuggestDbService suggestDbService;
    private final LessonDbService lessonDbService;
    private final MemberDbService memberDbService;
    private final FeignService feignService;

    /**
     * 신청 생성 메서드
     * @param post 신청 생성 DTO
     * @param lessonId 과외 식별자
     * @param memberId 사용자 식별자
     * @author LeeGoh
     */
    @Transactional
    public void createSuggest(PostSuggest post, Long lessonId, Long memberId) {
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

//        if(memberId.equals(findLesson.getMemberId())) {
//            throw new BusinessLogicException(ExceptionCode.TUTOR_CANNOT_RESERVATION);
//        }

        Suggest suggest = Suggest.builder()
                .days(post.getDays())
                .languages(post.getLanguages())
                .requests(post.getRequests())
                .lessonId(findLesson.getId())
                .memberId(memberId)
                .build();

        suggest.setStatus(ACCEPT_IN_PROGRESS);
        suggestDbService.saveSuggest(suggest);
    }

    /**
     * 신청 수락 메서드
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @param quantity 과외 횟수
     * @author LeeGoh
     */
    @Transactional
    public void acceptSuggest(Long suggestId, Long memberId, Integer quantity) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        if (!memberId.equals(findLesson.getMemberId()) ||
            !findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .quantity(quantity)
                .build();
        paymentInfo.setQuantityCount(0);
        paymentInfo.setSuggest(findSuggest);
        suggestDbService.savePayment(paymentInfo);

        findSuggest.setStartTime();
        findSuggest.setTotalCost(findLesson.getCost() * paymentInfo.getQuantity());
        findSuggest.setStatus(PAY_IN_PROGRESS);
        suggestDbService.saveSuggest(findSuggest);
    }

    /**
     * 신청 취소 메서드
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @author LeeGoh
     */
    public void cancelSuggest(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        if (!memberId.equals(findLesson.getMemberId())) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        if (!findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS) &&
            !findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        suggestDbService.deleteSuggest(findSuggest);
    }

    /**
     * 신청 거절 메서드
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @param postReason 거절 사유
     * @author LeeGoh
     */
    public void declineSuggest(Long suggestId, Long memberId, PostReason postReason) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        if (!memberId.equals(findLesson.getMemberId()) ||
            !findSuggest.getSuggestStatus().equals(ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        Reason reason = Reason.builder().reason(postReason.getReason()).build();
        suggestDbService.saveReason(reason);

        suggestDbService.deleteSuggest(findSuggest);
    }

    /*---------- 결제 로직 ----------*/

    /**
     * Kakao 결제 URL 요청 메서드
     * @param suggestId 신청 식별자
     * @param memberId 사용자 식별자
     * @param requestUrl 요청 URL
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getKaKapPayUrl(Long suggestId, Long memberId, String requestUrl) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        KakaoPayHeader headers = feignService.setKakaoHeaders();
        ReadyToKakaoPayInfo params =
                feignService.setReadyParams(requestUrl, findSuggest, findMember, findLesson, findPaymentInfo);

        KakaoPayReadyInfo payReadyInfo = feignService.getPayReadyInfo(headers, params);

        findPaymentInfo.setKakaoPaymentInfo(params, payReadyInfo.getTid());
        suggestDbService.savePayment(findPaymentInfo);

        return Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(KAKAO_PAY_URI_MSG)
                .build();
    }

    /**
     * Toss 결제 URL 요청 메서드
     * @param suggestId 신청 식별자
     * @param paymentId 결제 방법
     * @param requestUrl 요청 URL
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getTossPayUrl(Long suggestId, String requestUrl, int paymentId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getSuggestStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        String method = "";
        switch (paymentId) {
            case 2: method = "휴대폰";
                    break;
            case 3: method = "계좌이체";
                    break;
            default: method = "카드";
        }

        TossPayHeader headers = feignService.setTossHeaders();
        ReadyToTossPayInfo body =
                feignService.setReadyTossParams(requestUrl, findSuggest, findLesson, findPaymentInfo, method);

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
     * URL 요청 시 리턴되는 URL이 없을 때 발생하는 이벤트 메서드
     * @return Message
     * @author LeeGoh
     */
    public Message<?> getFailedPayMessage() {
        return Message.builder()
                .message(FAILED_INFO_MESSAGE + "<br>" + INVALID_PARAMS)
                .build();
    }

    /**
     * Kakao 결제 성공 시 예약 정보 반환 메서드
     * @param suggestId 신청 식별자
     * @param pgToken Payment Gateway Token
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

        return Message.builder()
                .data(kakaoPaySuccessInfo)
                .message(INFO_URI_MSG)
                .build();
    }

    /**
     * Toss 결제 성공 시 예약 정보 반환 메서드
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @Transactional
    public Message getSuccessTossPaymentInfo(Long suggestId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        TossPayHeader headers = feignService.setTossHeaders();
        RequestForTossPayInfo body = feignService.setRequestBody(findPaymentInfo);

        TossPaySuccessInfo tossPaySuccessInfo = feignService.getSuccessTossResponse(headers, body);

        switch (tossPaySuccessInfo.getMethod()) {
            case "휴대폰": findSuggest.setPaymentMethod("토스페이 휴대폰");
                break;
            case "계좌이체": findSuggest.setPaymentMethod("토스페이 계좌이체");
                break;
            default: findSuggest.setPaymentMethod("토스페이 카드");
        }

        tossPaySuccessInfo.setOrderStatus(ORDER_APPROVED);
        findSuggest.setStatus(DURING_LESSON);
        suggestDbService.saveSuggest(findSuggest);

        return Message.builder()
                .data(tossPaySuccessInfo)
                .message(INFO_URI_MSG)
                .build();
    }

    /**
     * 과외 종료 시 신청 상태 및 종료 시간 저장 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    public void setSuggestStatusAndEndTime(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        if (!findSuggest.getSuggestStatus().equals(DURING_LESSON) ||
            !findLesson.getMemberId().equals(memberId)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        findSuggest.setStatus(END_OF_LESSON);
        findSuggest.setEndTime();
        suggestDbService.saveSuggest(findSuggest);
    }

    /**
     * 환불 메서드 Kakao/Toss
     * @param suggestId 신청 식별자
     * @param memberId 회원 식별자
     * @return Message
     * @author LeeGoh
     */
    public Message refundPaymentKakaoOrToss(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        suggestDbService.suggestGetMemberIdAndStatusIsDuringLesson(findSuggest, memberId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        if (findPaymentInfo.getQuantity() == findPaymentInfo.getQuantityCount()) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        switch (findSuggest.getPaymentMethod()) {
            case "카카오페이": return refundKakaoPayment(findSuggest, findPaymentInfo);
            default: return refundTossPayment(findSuggest, findPaymentInfo);
        }
    }

    /**
     * Kakao 환불 메서드
     * @param suggest 신청 정보
     * @param paymentInfo 결제 정보
     * @author LeeGoh
     */
    public Message refundKakaoPayment(Suggest suggest, PaymentInfo paymentInfo) {
        KakaoPayHeader headers = feignService.setKakaoHeaders();
        RequestForKakaoPayCancelInfo params = feignService.setRequestCancelParams(paymentInfo);

        KakaoPayCancelInfo cancelInfo =
                feignService.getCancelKakaoPaymentResponse(headers, params);

        cancelInfo.setOrderStatus(REFUND_APPROVED);
        // suggest.setStatus(REFUND_PAYMENT);
        suggest.setEndTime();
        suggestDbService.saveSuggest(suggest);

        return Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();
    }

    /**
     * Toss 환불 메서드
     * @param suggest 신청 정보
     * @param paymentInfo 결제 정보
     * @author LeeGoh
     */
    public Message refundTossPayment(Suggest suggest, PaymentInfo paymentInfo) {
        TossPayHeader headers = feignService.setTossHeaders();
        RequestForTossPayCancelInfo body = feignService.setCancelBody(paymentInfo);

        TossPayCancelInfo cancelInfo =
                feignService.getCancelTossPaymentResponse(headers, paymentInfo.getPaymentKey(), body);

        cancelInfo.setOrderStatus(REFUND_APPROVED);
        // suggest.setStatus(REFUND_PAYMENT);
        suggest.setEndTime();
        suggestDbService.saveSuggest(suggest);

        return Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();
    }

    /**
     * 환불 단건 조회 메서드
     * @param suggestId 신청 식별자
     * @param memberId 회원 식별자
     * @return GetRefundPayment
     * @author LeeGoh
     */
    public GetRefundPayment getRefundPaymentInfo(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getId());
        Member findTutor = memberDbService.ifExistsReturnMember(findLesson.getMemberId());
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        if (!findSuggest.getMemberId().equals(memberId) ||
            !findSuggest.getSuggestStatus().equals(REFUND_PAYMENT)) {
            throw new BusinessLogicException(INVALID_ACCESS);
        }

        return GetRefundPayment.builder()
                .suggest(findSuggest)
                .tutorName(findTutor.getName())
                .lesson(findLesson)
                .paymentInfo(findPaymentInfo)
                .build();
    }

    /*---------- 출석부 로직 ----------*/

    /**
     * 출석 인정 메서드
     * @param suggestId 신청 식별자
     * @param memberId 회원 식별자
     * @return Integer
     * @author LeeGoh
     */
    public Integer teacherChecksAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        suggestDbService.lessonGetMemberIdAndStatusIsDuringLesson(findSuggest, findLesson, memberId);

        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);
        suggestDbService.checkQuantityCount(findPaymentInfo);

        return findPaymentInfo.getQuantityCount();
    }

    /**
     * 출석 인정 취소 메서드
     * @param suggestId 신청 식별자
     * @param memberId 회원 식별자
     * @return Integer
      @author LeeGoh
     */
    public Integer teacherCancelAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());
        suggestDbService.lessonGetMemberIdAndStatusIsDuringLesson(findSuggest, findLesson, memberId);

        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);
        suggestDbService.cancelQuantityCount(findPaymentInfo);

        return findPaymentInfo.getQuantityCount();
    }

    /**
     * 출석부 조회 메서드
     * @param suggestId 신청 식별자
     * @param memberId 회원 식별자
     * @return Integer
      @author LeeGoh
     */
    public GetLessonAttendance getLessonAttendance(Long suggestId, Long memberId) {
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        suggestDbService.suggestGetMemberIdAndStatusIsNotInProgress(findSuggest, memberId);
        PaymentInfo findPaymentInfo = suggestDbService.ifExistsReturnPaymentInfo(suggestId);

        Integer quantity = findPaymentInfo.getQuantity();
        Integer quantityCount = findPaymentInfo.getQuantityCount();

        return GetLessonAttendance.builder()
                .quantity(quantity)
                .quantityCount(quantityCount)
                .progress((int)((double)quantityCount/(double)quantity * 100))
                .build();
    }
}
