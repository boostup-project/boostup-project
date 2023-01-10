package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.kakao.Amount;
import com.codueon.boostUp.domain.suggest.kakao.KakaoCard;
import com.codueon.boostUp.domain.suggest.kakao.KakaoPayReadyInfo;
import com.codueon.boostUp.domain.suggest.kakao.KakaoPaySuccessInfo;
import com.codueon.boostUp.domain.suggest.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KakaoPaymentTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 4 결제 URL 요청")
    void orderPayment() throws Exception {

        KakaoPayReadyInfo payReadyInfo = KakaoPayReadyInfo.builder()
                .nextRedirectPcUrl("nextRedirectPcUrl")
                .build();

        Message<?> message = Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(KAKAO_PAY_URI_MSG)
                .build();

        given(suggestService.getKaKapPayUrl(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/kakao/payment", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 5 결제 성공")
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

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 6 결제 취소")
    void cancelPayment() throws Exception {

        String cancelMessage = CANCELED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/kakao/cancellation", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 7 결제 실패")
    void failedPayment() throws Exception {

        String failMessage = FAILED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/kakao/failure", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

}
