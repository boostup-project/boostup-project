package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.toss.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TossPaymentTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 4-2 Toss 결제 URL 요청")
    void orderPayment() throws Exception {
        int paymentId = 1;

        Checkout checkout = Checkout.builder()
                .url("nextRedirectPcUrl")
                .build();

        TossPayReadyInfo payReadyInfo = TossPayReadyInfo.builder()
                .checkout(checkout)
                .build();

        Message<?> message = Message.builder()
                .data(payReadyInfo.getCheckout().getUrl())
                .message(TOSS_PAY_URI_MSG)
                .build();

        given(suggestService.getTossPayUrl(Mockito.anyLong(), Mockito.anyInt()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/toss/payment/{payment-id}", suggest.getId(), paymentId)
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
                .andExpect(jsonPath("$.data").value(message.getData()))
                .andDo(document("신청4.2-토스결제요청",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자"),
                                parameterWithName("payment-id").description("결제 정보 식별자")
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
    @DisplayName("GET 신청 프로세스 5-2 Toss 결제 성공")
    void successTossPayment() throws Exception {
        Checkout checkout = Checkout.builder().build();
        TossCard tossCard = TossCard.builder().build();
        Receipt receipt = Receipt.builder().build();
        MobilePhone mobilePhone = MobilePhone.builder().build();
        Transfer transfer = Transfer.builder().build();

        TossPaySuccessInfo tossPaySuccessInfo = TossPaySuccessInfo.builder()
                .totalAmount(5000)
                .paymentKey("paymentKey")
                .lastTransactionKey("lastTransactionKey")
                .method("카드")
                .orderId("orderId")
                .orderName("orderName")
                .checkout(checkout)
                .mId("mId")
                .requestedAt("requestedAt")
                .approvedAt("approvedAt")
                .card(tossCard)
                .receipt(receipt)
                .mobilePhone(mobilePhone)
                .transfer(transfer)
                .orderStatus("orderStatus")
                .build();

        Message<?> message = Message.builder()
                .data(tossPaySuccessInfo)
                .message(INFO_URI_MSG)
                .build();

        given(suggestService.getSuccessTossPaymentInfo(Mockito.anyLong()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/toss/success", suggest.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청5.2-토스결제성공",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 7-2 Toss 결제 실패")
    void failedTossPayment() throws Exception {
        String failMessage = FAILED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/toss/failure", suggest.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청7.2-토스결제실패",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 9-2 Toss 환불")
    void refundPaymentKakaoOrToss() throws Exception {
        Integer count = 2;
        Integer amount = 5000;

        RequestForTossPayCancelInfo body = RequestForTossPayCancelInfo.builder()
                .cancelReason("고객이 취소를 요청함")
                .cancelAmount(count * amount)
                .build();

        List<Cancels> cancels = new ArrayList<>();
        Checkout checkout = Checkout.builder().build();
        TossCard card = TossCard.builder().build();
        Receipt receipt = Receipt.builder().build();
        MobilePhone mobilePhone = MobilePhone.builder().build();
        Transfer transfer = Transfer.builder().build();

        TossPayCancelInfo cancelInfo = TossPayCancelInfo.builder()
                .cancels(cancels)
                .totalAmount(count * amount)
                .paymentKey("paymentKey")
                .lastTransactionKey("lastTransactionKey")
                .method("method")
                .orderId("orderId")
                .orderName("orderName")
                .checkout(checkout)
                .mId("mId")
                .requestedAt("requestedAt")
                .approvedAt("approvedAt")
                .card(card)
                .receipt(receipt)
                .mobilePhone(mobilePhone)
                .transfer(transfer)
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
                .andDo(document("신청9.2-토스결제환불",
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
