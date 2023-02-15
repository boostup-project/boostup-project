package com.codueon.boostUp.domain.suggest.kakao;

import com.codueon.boostUp.domain.suggest.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

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

public class KakaoPaymentTest extends KakaoPayControllerTest {

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

        given(kakaoPayService.getKaKaoPayUrl(Mockito.anyLong())).willReturn(message);

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

        Message<?> message = Message.builder()
                .data(getKakaoPaySuccessInfo())
                .message(INFO_URI_MSG)
                .build();

        given(kakaoPayService.getSuccessKakaoPaymentInfo(Mockito.anyLong(), Mockito.anyString()))
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
}
