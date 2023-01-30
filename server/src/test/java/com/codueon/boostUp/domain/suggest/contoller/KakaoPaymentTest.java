package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.kakao.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KakaoPaymentTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 4-1 Kakao 결제 URL 요청")
    void orderPayment() throws Exception {
        KakaoPayReadyInfo payReadyInfo = KakaoPayReadyInfo.builder()
                .nextRedirectPcUrl("nextRedirectPcUrl")
                .build();

        Message<?> message = Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(KAKAO_PAY_URI_MSG)
                .build();

        given(suggestService.getKaKapPayUrl(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/kakao/payment", suggest.getId())
                                .header("Authorization", BEARER + accessToken)
                                .header("RefreshToken", refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청4.1-카카오결제요청",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                messageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 5-1 Kakao 결제 성공")
    void successPayment() throws Exception {
        String pgToken = "pgToken";

        Date date = new Date(2023, 01, 05, 05, 00);
        KakaoCard cardInfo = KakaoCard.builder().build();
        Amount amount = Amount.builder().build();

        KakaoPaySuccessInfo kakaoPaySuccessInfo = KakaoPaySuccessInfo.builder()
                .aid("aid")
                .tid("tid")
                .cid("cid")
                .sid("sid")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("paymentMethodType")
                .amount(amount)
                .kakaoCard(cardInfo)
                .itemName("itemName")
                .itemCode("itemCode")
                .payload("payload")
                .quantity(3)
                .taxFreeAmount(5)
                .vatAmount(3)
                .createdAt(date)
                .approvedAt(date)
                .orderStatus("orderStatus")
                .build();

        Message<?> message = Message.builder()
                .data(kakaoPaySuccessInfo)
                .message(INFO_URI_MSG)
                .build();

        given(suggestService.getSuccessKakaoPaymentInfo(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/kakao/success", suggest.getId())
                                .param("pg_token", pgToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청5.1-카카오결제요청",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 6 Kakao 결제 취소")
    void cancelPayment() throws Exception {
        String cancelMessage = CANCELED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/kakao/cancellation", suggest.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청6-카카오결제취소",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 7-1 Kakao 결제 실패")
    void failedPayment() throws Exception {
        String failMessage = FAILED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/kakao/failure", suggest.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청7.1-카카오결제실패",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 9-1 Kakao 환불")
    void refundPaymentKakaoOrToss() throws Exception {
        Integer count = 2;
        Integer cost = 5000;

        RequestForKakaoPayCancelInfo param = RequestForKakaoPayCancelInfo.builder()
                .tid("tid")
                .cid("cid")
                .cancel_tax_free_amount(1000)
                .cancel_amount(count * cost)
                .build();

        Amount amount = Amount.builder()
                .total(suggest.getTotalCost())
                .taxFree(param.getCancel_tax_free_amount())
                .vat(1000)
                .discount(0)
                .point(0)
                .build();

        ApprovedCancelAmount approvedCancelAmount = ApprovedCancelAmount.builder()
                .total(param.getCancel_amount())
                .vat(1000)
                .build();

        CanceledAmount canceledAmount = CanceledAmount.builder()
                .total(param.getCancel_amount())
                .vat(1000)
                .build();

        CancelAvailableAmount cancelAvailableAmount = CancelAvailableAmount.builder()
                .total(param.getCancel_amount())
                .build();

        Date date = new Date(2023, 01, 19, 05, 25);

        KakaoPayCancelInfo cancelInfo = KakaoPayCancelInfo.builder()
                .aid("aid")
                .tid(param.getTid())
                .cid(param.getCid())
                .status("status")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("paymentMethodType")
                .amount(amount)
                .approvedCancelAmount(approvedCancelAmount)
                .canceledAmount(canceledAmount)
                .cancelAvailableAmount(cancelAvailableAmount)
                .itemName("itemName")
                .itemCode("itemCode")
                .quantity(paymentInfo.getQuantity())
                .createdAt(date)
                .approvedAt(date)
                .canceledAt(date)
                .orderStatus("orderStatus")
                .build();

        Message message = Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();

        given(suggestService.refundPaymentKakaoOrToss(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/refund", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청9.1-카카오결제환불",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }

}
