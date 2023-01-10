package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.toss.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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

        given(suggestService.getTossPayUrl(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/toss/payment/{payment-id}", suggest.getId(), paymentId)
                );

        actions.andExpect(status().isOk())
                .andReturn();

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

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 7-2 Toss 결제 실패")
    void failedTossPayment() throws Exception {

        String failMessage = FAILED_PAY_MESSAGE;

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/toss/failure", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

}
