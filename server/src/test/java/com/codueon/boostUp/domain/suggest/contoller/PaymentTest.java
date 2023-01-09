package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.suggest.dto.GetSuggestInfo;
import com.codueon.boostUp.domain.suggest.dto.PostReason;
import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import com.codueon.boostUp.domain.suggest.pay.Amount;
import com.codueon.boostUp.domain.suggest.pay.CardInfo;
import com.codueon.boostUp.domain.suggest.pay.PayReadyInfo;
import com.codueon.boostUp.domain.suggest.pay.PaySuccessInfo;
import com.codueon.boostUp.domain.suggest.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentTest extends SuggestControllerTest{

    @Test
    @DisplayName("POST 신청 프로세스 1 - 과외 신청")
    void createSuggest() throws Exception{

        Long lessonId = 1L;

        PostSuggest post = PostSuggest.builder()
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .build();

        String content = gson.toJson(post);

        doNothing().when(suggestService).createSuggest(Mockito.any(), Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        post("/lesson/{lesson-id}/suggest", lessonId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())

                );

        actions.andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    @DisplayName("POST 신청 프로세스 2-1 수락하기")
    void acceptSuggest() throws Exception{

        Integer quantity = 1;

        String content = gson.toJson(quantity);

        doNothing().when(suggestService).acceptSuggest(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt());

        ResultActions actions =
                mockMvc.perform(
                        post("/lesson/suggest/{suggest-id}/accept", suggest.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST 신청 프로세스 2-2 거절하기")
    void declineSuggest() throws Exception {

        Long lessonId = 1L;

        PostReason postReason = PostReason.builder()
                .reason("배울 자세가 안 되어있음 쯧")
                .build();

        String content = gson.toJson(postReason);

        doNothing().when(suggestService).declineSuggest(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());

        ResultActions actions =
                mockMvc.perform(
                        post("/lesson/suggest/{suggest-id}/decline", suggest.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions.andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("GET 신청 프로세스 3 결제 페이지 조회")
    void getSuggestInfo() throws Exception {

        Integer quantity = 5;
        Integer totalCost = 250000;

        ProfileImage profileImage = ProfileImage.builder()
                .filePath("gddong.jpg")
                .build();

        Lesson lesson = Lesson.builder()
                .id(1L)
                .title("Java에게 뿌셔지기")
                .name("명품강사 길동씨")
                .company("네카라쿠배 가고싶다")
                .cost(50000)
                .build();

        lesson.addProfileImage(profileImage);

        GetSuggestInfo getSuggestInfo =
                new GetSuggestInfo(lesson, totalCost, quantity);

        given(suggestService.getSuggestInfo(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getSuggestInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/suggest/{suggest-id}/suggest-info", suggest.getLessonId())
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("GET 신청 프로세스 4 결제 URL 요청")
    void orderPayment() throws Exception {

        PayReadyInfo payReadyInfo = PayReadyInfo.builder()
                .nextRedirectPcUrl("nextRedirectPcUrl")
                .build();

        Message<?> message = Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(PAY_URI_MSG)
                .build();

        given(suggestService.getKaKapPayUrl(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/suggest/{suggest-id}/payment", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 5 결제 성공")
    void successPayment() throws Exception {

        String pgToken = "pgToken";

        Date date = new Date(2023, 01, 05, 05, 00);
        CardInfo cardInfo = CardInfo.builder().build();
        Amount amount = Amount.builder().build();

        PaySuccessInfo paySuccessInfo = PaySuccessInfo.builder()
                .aid("aid")
                .tid("tid")
                .cid("cid")
                .sid("sid")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("paymentMethodType")
                .amount(amount)
                .cardInfo(cardInfo)
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
                .data(paySuccessInfo)
                .message(INFO_URI_MSG)
                .build();

        given(suggestService.getSuccessPaymentInfo(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/api/suggest/{suggest-id}/completed", suggest.getId())
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
                        get("/api/suggest/{suggest-id}/cancel", suggest.getId())
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
                        get("/api/suggest/{suggest-id}/fail", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 신청 프로세스 8 과외 종료")
    void endOfLesson() throws Exception {

        doNothing().when(suggestService).setSuggestStatusAndEndTime(suggest.getId());

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/suggest/{suggest-id}/done", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }

}
