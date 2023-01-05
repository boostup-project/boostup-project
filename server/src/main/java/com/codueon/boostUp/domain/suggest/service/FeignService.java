package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.feign.KakaoPayFeignClient;
import com.codueon.boostUp.domain.suggest.pay.KakaoPayHeader;
import com.codueon.boostUp.domain.suggest.pay.PayReadyInfo;
import com.codueon.boostUp.domain.suggest.pay.ReadyToPaymentInfo;
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


    public KakaoPayHeader setHeaders() {
        return KakaoPayHeader.builder()
                .adminKey(PayConstants.KAKAO_AK + adminKey)
                .accept(MediaType.APPLICATION_JSON + PayConstants.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + PayConstants.UTF_8)
                .build();
    }

    public ReadyToPaymentInfo setParams(String requestUrl, Suggest suggest, Member member, Lesson lesson) {
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

}
