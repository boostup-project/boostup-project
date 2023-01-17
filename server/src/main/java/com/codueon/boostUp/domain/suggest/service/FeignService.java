package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.feign.KakaoPayFeignClient;
import com.codueon.boostUp.domain.suggest.feign.TossPayFeignClient;
import com.codueon.boostUp.domain.suggest.kakao.*;
import com.codueon.boostUp.domain.suggest.toss.*;
import com.codueon.boostUp.domain.suggest.utils.PayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class FeignService {

    @Value("${kakao.admin.key}")
    private String adminKey;

    @Value("${kakao.uri.pay-process}")
    private String paymentProcessUri;

    @Value("${kakao.pay.cid}")
    private String cid;

    @Value("${kakao.pay.taxfree}")
    private Integer taxFreeAmount;

    @Value("${toss.secret-key}")
    private String SECRET_KEY;

    @Autowired
    KakaoPayFeignClient kakaoFeignClient;

    @Autowired
    TossPayFeignClient tossPayFeignClient;


    /**
     * Kakao 결제 헤더 입력 메서드
     * @return KakaoPayHeader
     * @author LeeGoh
     */
    public KakaoPayHeader setKakaoHeaders() {
        return KakaoPayHeader.builder()
                .adminKey(PayConstants.KAKAO_AK + adminKey)
                .accept(MediaType.APPLICATION_JSON + PayConstants.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + PayConstants.UTF_8)
                .build();
    }

    /**
     * Toss 결제 헤더 입력 메서드
     * @return TossPayHeader
     * @author LeeGoh
     */
    public TossPayHeader setTossHeaders() {
        return TossPayHeader.builder()
                .adminKey(PayConstants.TOSS_AK + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Kakao 결제 전 파라미터 입력 메서드
     * @param requestUrl 요청 URL
     * @param suggest 신청 정보
     * @param member 사용자 정보
     * @param lesson 과외 정보
     * @param paymentInfo 결제 정보
     * @return ReadyToKakaoPaymentInfo
     * @author LeeGoh
     */
    public ReadyToKakaoPaymentInfo setReadyParams(String requestUrl, Suggest suggest, Member member, Lesson lesson, PaymentInfo paymentInfo) {
        return ReadyToKakaoPaymentInfo.builder()
                .cid(cid)
                .approval_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/kakao/success")
                .cancel_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/kakao/cancellation")
                .fail_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/kakao/failure")
                .partner_order_id(suggest.getId() + "/" + member.getId() + "/" + lesson.getTitle())
                .partner_user_id(member.getId().toString())
                .item_name(lesson.getTitle())
                .quantity(paymentInfo.getQuantity())
                .total_amount(paymentInfo.getQuantity() * lesson.getCost())
                .val_amount(suggest.getTotalCost())
                .tax_free_amount(taxFreeAmount)
                .build();
    }

    /**
     * Toss 결제 전 파라미터 입력 메서드
     * @param requestUrl 요청 URL
     * @param suggest 신청 정보
     * @param paymentInfo 결제 정보
     * @param lesson 과외 정보
     * @return ReadyToTossPaymentInfo
     * @author LeeGoh
     */
    public ReadyToTossPaymentInfo setReadyTossParams(String requestUrl, Suggest suggest, Lesson lesson, PaymentInfo paymentInfo, String method) {
        return ReadyToTossPaymentInfo.builder()
                .successUrl(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/toss/success")
                .failUrl(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/toss/failure")
                .amount(paymentInfo.getQuantity() * lesson.getCost())
                .method(method)
                .orderId(UUID.randomUUID().toString())
                .orderName(lesson.getTitle())
                .build();
    }

    /**
     * Kakao 결제 URL 생성 결과 메서드
     * @param headers KakaoPayHeader
     * @param params ReadyToPaymentInfo
     * @return KakaoPayReadyInfo
     * @author LeeGoh
     */
    public KakaoPayReadyInfo getPayReadyInfo(KakaoPayHeader headers,
                                             ReadyToKakaoPaymentInfo params) {
        try {
            return kakaoFeignClient.readyForPayment(
                    headers.getAdminKey(),
                    headers.getAccept(),
                    headers.getContentType(),
                    params
            );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Toss 결제 URL 생성 결과 메서드
     * @param headers TossPayHeader
     * @param body ReadyToTossPaymentInfo
     * @return TossPayReadyInfo
     * @author LeeGoh
     */
    public TossPayReadyInfo getTossPayReadyInfo(TossPayHeader headers,
                                                ReadyToTossPaymentInfo body) {
        try {
            return tossPayFeignClient.readyForTossPayment(
                    headers.getAdminKey(),
                    headers.getContentType(),
                    body
            );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Kakao 결제 후 예약 정보 조회를 위한 파라미터 입력 메서드
     * @param pgToken Payment Gateway Token
     * @param paymentInfo 결제 정보
     * @return RequestForKakaoPaymentInfo
     * @author LeeGoh
     */
    public RequestForKakaoPaymentInfo setRequestParams(String pgToken, PaymentInfo paymentInfo) {
        return RequestForKakaoPaymentInfo.builder()
                .cid(paymentInfo.getCid())
                .tid(paymentInfo.getTid())
                .partner_order_id(paymentInfo.getPartnerOrderId())
                .partner_user_id(paymentInfo.getPartnerUserId())
                .pg_token(pgToken)
                .total_amount(paymentInfo.getTotalAmount())
                .build();
    }

    /**
     * Toss 결제 후 예약 정보 조회를 위한 파라미터 입력 메서드
     * @param paymentInfo 결제 정보
     * @return RequestForTossPaymentInfo
     * @author LeeGoh
     */
    public RequestForTossPaymentInfo setRequestBody(PaymentInfo paymentInfo) {
        return RequestForTossPaymentInfo.builder()
                .paymentKey(paymentInfo.getPaymentKey())
                .amount(paymentInfo.getAmount())
                .orderId(paymentInfo.getOrderId())
                .build();
    }

    /**
     * Toss 환불 정보 바디 입력 메서드
     * @param paymentInfo 결제 정보
     * @return CancelToTossPaymentInfo
     * @author LeeGoh
     */
    public CancelToTossPaymentInfo setCancelBody(PaymentInfo paymentInfo) {

        Integer count = paymentInfo.getQuantity() - paymentInfo.getQuantityCount();
        Integer amount = paymentInfo.getAmount();

        if (paymentInfo.getQuantityCount() > 0) {
            return CancelToTossPaymentInfo.builder()
                    .cancelReason("고객이 취소를 원함")
                    .cancelAmount(count * amount)
                    .build();
        } else return CancelToTossPaymentInfo.builder()
                .cancelReason("고객이 취소를 원함")
                .build();

    }

    /**
     * Kakao 결제 완료 후 신청 정보 요청 메서드
     * @param headers KakaoPayHeader
     * @param params RequestForKakaoPaymentInfo
     * @return KakaoPaySuccessInfo
     * @author LeeGoh
     */
    public KakaoPaySuccessInfo getSuccessKakaoResponse(KakaoPayHeader headers, RequestForKakaoPaymentInfo params) {

        try {
            return kakaoFeignClient
                    .successForPayment(
                            headers.getAdminKey(),
                            headers.getAccept(),
                            headers.getContentType(),
                            params
                    );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;

    }

    /**
     * Toss 결제 완료 후 신청 정보 요청 메서드
     * @param headers TossPayHeader
     * @param body RequestForTossPaymentInfo
     * @return TossPaySuccessInfo
     * @author LeeGoh
     */
    public TossPaySuccessInfo getSuccessTossResponse(TossPayHeader headers, RequestForTossPaymentInfo body) {

        try {
            return tossPayFeignClient
                    .successForPayment(
                            headers.getAdminKey(),
                            headers.getContentType(),
                            body
                    );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;

    }

    /**
     * Toss 환불 완료 후 환불 정보 요청 메서드
     * @param headers TossPayHeader
     * @param paymentKey paymentKey
     * @param body CancelToTossPaymentInfo
     * @return TossPayCancelInfo
     * @author LeeGoh
     */
    public TossPayCancelInfo getCancelPaymentResponse(TossPayHeader headers, String paymentKey, CancelToTossPaymentInfo body) {

        try {
            return tossPayFeignClient
                    .cancelPayment(
                            headers.getAdminKey(),
                            headers.getContentType(),
                            paymentKey,
                            body
                    );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;

    }

}
