package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.feign.KakaoPayFeignClient;
import com.codueon.boostUp.domain.suggest.pay.*;
import com.codueon.boostUp.domain.suggest.utils.PayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

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

    @Autowired
    KakaoPayFeignClient kakaoFeignClient;


    /**
     * 카카오페이 결제 헤더 입력 메서드
     * @return KakaoPayHeader
     * @author LeeGoh
     */
    public KakaoPayHeader setHeaders() {
        return KakaoPayHeader.builder()
                .adminKey(PayConstants.KAKAO_AK + adminKey)
                .accept(MediaType.APPLICATION_JSON + PayConstants.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + PayConstants.UTF_8)
                .build();
    }

    /**
     * 카카오페이 결제 전 파라미터 입력 메서드
     * @param requestUrl 요청 URL
     * @param suggest 신청 정보
     * @param member 사용자 정보
     * @param lesson 과외 정보
     * @return ReadyToPaymentInfo
     * @author LeeGoh
     */
    public ReadyToPaymentInfo setReadyParams(String requestUrl, Suggest suggest, Member member, Lesson lesson) {
        return ReadyToPaymentInfo.builder()
                .cid(cid)
                .approval_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/completed")
                .cancel_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/cancel")
                .fail_url(requestUrl + paymentProcessUri + "/" + suggest.getId() + "/fail")
                .partner_order_id(suggest.getId() + "/" + member.getId() + "/" + lesson.getTitle())
                .partner_user_id(member.getId().toString())
                .item_name(lesson.getTitle())
                .quantity(suggest.getQuantity())
                .total_amount(suggest.getQuantity() * lesson.getCost())
                .val_amount(suggest.getTotalCost())
                .tax_free_amount(taxFreeAmount)
                .build();
    }

    /**
     * 결제 URL 생성 결과 메서드
     * @param headers KakaoPayHeader
     * @param params ReadyToPaymentInfo
     * @return PayReadyInfo
     * @author LeeGoh
     */
    public PayReadyInfo getPayReadyInfo(KakaoPayHeader headers,
                                        ReadyToPaymentInfo params) {
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
     * 결제 후 예약 정보 조회를 위한 파라미터 입력 메서드
     * @param pgToken Payment Gateway Token
     * @param paymentInfo 결제 정보
     * @return RequestForPaymentInfo
     * @author LeeGoh
     */
    public RequestForPaymentInfo setRequestParams(String pgToken, PaymentInfo paymentInfo) {
        return RequestForPaymentInfo.builder()
                .cid(paymentInfo.getCid())
                .tid(paymentInfo.getTid())
                .partner_order_id(paymentInfo.getPartnerOrderId())
                .partner_user_id(paymentInfo.getPartnerUserId())
                .pg_token(pgToken)
                .total_amount(paymentInfo.getTotalAmount())
                .build();
    }

    /**
     * 결제 완료 후 신청 정보 요청 메서드
     * @param headers KakaoPayHeader
     * @param params RequestForPaymentInfo
     * @return PaySuccessInfo
     * @author LeeGoh
     */
    public PaySuccessInfo getSuccessResponse(KakaoPayHeader headers, RequestForPaymentInfo params) {

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

}
