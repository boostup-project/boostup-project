package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
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
     * @param suggestId 신청 식별자
     * @param totalCost 총 가격
     * @param memberId 사용자 식별자
     * @param title 과외 타이틀
     * @param cost 과외 가격
     * @param quantity 과외 횟수
     * @return ReadyToKakaoPaymentInfo
     * @author LeeGoh
     */
    public ReadyToKakaoPayInfo setReadyParams(String requestUrl,
                                              Long suggestId,
                                              Integer totalCost,
                                              Long memberId,
                                              String title,
                                              Integer cost,
                                              Integer quantity) {
        return ReadyToKakaoPayInfo.builder()
                .cid(cid)
                .approval_url(requestUrl + paymentProcessUri + "/" + suggestId + "/kakao/success")
                .cancel_url(requestUrl + paymentProcessUri + "/" + suggestId + "/kakao/cancellation")
                .fail_url(requestUrl + paymentProcessUri + "/" + suggestId + "/kakao/failure")
                .partner_order_id(suggestId + "/" + memberId + "/" + title)
                .partner_user_id(memberId.toString())
                .item_name(title)
                .quantity(quantity)
                .total_amount(quantity * cost)
                .val_amount(totalCost)
                .tax_free_amount(taxFreeAmount)
                .build();
    }

    /**
     * Toss 결제 전 파라미터 입력 메서드
     * @param requestUrl 요청 URL
     * @param suggestId 신청 식별자
     * @param cost 과외 가격
     * @param quantity 과외 횟수
     * @param title 과외 타이틀
     * @return ReadyToTossPaymentInfo
     * @author LeeGoh
     */
    public ReadyToTossPayInfo setReadyTossParams(String requestUrl,
                                                 Long suggestId,
                                                 Integer cost,
                                                 String title,
                                                 Integer quantity,
                                                 String method) {
        return ReadyToTossPayInfo.builder()
                .successUrl(requestUrl + paymentProcessUri + "/" + suggestId + "/toss/success")
                .failUrl(requestUrl + paymentProcessUri + "/" + suggestId + "/toss/failure")
                .amount(quantity * cost)
                .method(method)
                .orderId(UUID.randomUUID().toString())
                .orderName(title)
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
                                             ReadyToKakaoPayInfo params) {
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
                                                ReadyToTossPayInfo body) {
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
    public RequestForKakaoPayInfo setRequestParams(String pgToken, PaymentInfo paymentInfo) {
        return RequestForKakaoPayInfo.builder()
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
    public RequestForTossPayInfo setRequestBody(PaymentInfo paymentInfo) {
        return RequestForTossPayInfo.builder()
                .paymentKey(paymentInfo.getPaymentKey())
                .amount(paymentInfo.getAmount())
                .orderId(paymentInfo.getOrderId())
                .build();
    }

    public RequestForKakaoPayCancelInfo setRequestCancelParams(PaymentInfo paymentInfo) {
        Integer count = paymentInfo.getQuantity() - paymentInfo.getQuantityCount();
        Integer amount = paymentInfo.getTotalAmount() / paymentInfo.getQuantity();

        return RequestForKakaoPayCancelInfo.builder()
                .tid(paymentInfo.getTid())
                .cid(paymentInfo.getCid())
                .cancel_amount(count * amount)
                .cancel_tax_free_amount(1000)
                .build();
    }

    /**
     * Toss 환불 정보 바디 입력 메서드
     * @param paymentInfo 결제 정보
     * @return CancelToTossPaymentInfo
     * @author LeeGoh
     */
    public RequestForTossPayCancelInfo setCancelBody(PaymentInfo paymentInfo) {
        Integer count = paymentInfo.getQuantity() - paymentInfo.getQuantityCount();
        Integer amount = paymentInfo.getAmount();

        if (paymentInfo.getQuantityCount() > 0) {
            return RequestForTossPayCancelInfo.builder()
                    .cancelReason("고객이 취소를 원함")
                    .cancelAmount(count * amount)
                    .build();
        } else return RequestForTossPayCancelInfo.builder()
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
    public KakaoPaySuccessInfo getSuccessKakaoResponse(KakaoPayHeader headers,
                                                       RequestForKakaoPayInfo params) {
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
    public TossPaySuccessInfo getSuccessTossResponse(TossPayHeader headers,
                                                     RequestForTossPayInfo body) {
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

    public KakaoPayCancelInfo getCancelKakaoPaymentResponse(KakaoPayHeader headers,
                                                            RequestForKakaoPayCancelInfo params) {
        try {
            return kakaoFeignClient
                    .cancelForPayment(
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
     * Toss 환불 완료 후 환불 정보 요청 메서드
     * @param headers TossPayHeader
     * @param paymentKey paymentKey
     * @param body CancelToTossPaymentInfo
     * @return TossPayCancelInfo
     * @author LeeGoh
     */
    public TossPayCancelInfo getCancelTossPaymentResponse(TossPayHeader headers, String paymentKey, RequestForTossPayCancelInfo body) {

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
