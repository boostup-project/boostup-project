package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestGetTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 3 결제 페이지 조회")
    void getPaymentInfo() throws Exception {
        Integer quantity = 5;
        Integer totalCost = 250000;
        String name = "김선생";

        lesson.addProfileImage(profileImage);

        GetPaymentInfo getPaymentInfo =
                new GetPaymentInfo(lesson, name, totalCost, quantity);

        given(suggestDbService.getPaymentInfoOnMyPage(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/payment/info", suggest.getLessonId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(getPaymentInfo.getTotalCost()))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("신청3-결제페이지",
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
                                getPaymentInfoResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 8 과외 종료")
    void endOfLesson() throws Exception {
        doNothing().when(suggestService).setSuggestStatusAndEndTime(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/done", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청8-과외종료",
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

    @Test
    @DisplayName("GET 환불 영수증 조회")
    void getRefundPaymentInfo() throws Exception {
        String name = "김선생";

        GetRefundPayment response = GetRefundPayment.builder()
                .suggest(suggest)
                .name(name)
                .lesson(lesson)
                .paymentInfo(paymentInfo)
                .build();

        given(suggestService.getRefundPaymentInfo(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/refund/info", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.quantityCount").value(response.getQuantityCount()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.company").value(response.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(response.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(response.getCost()))
                .andExpect(jsonPath("$.totalCost").value(response.getTotalCost()))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("환불영수증조회",
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
                                getRefundPaymentInfoResponse()
                        )
                ));

    }

    @Test
    @DisplayName("GET 출석부 1 출석부 조회")
    void getLessonAttendance() throws Exception {
        GetLessonAttendance response = GetLessonAttendance.builder()
                .quantity(5)
                .quantityCount(1)
                .progress(20)
                .build();

        given(suggestService.getLessonAttendance(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/attendance", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.quantityCount").value(response.getQuantityCount()))
                .andExpect(jsonPath("$.progress").value(response.getProgress()))
                .andDo(document("출석부1-출석조회",
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
                                List.of(
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("과외 횟수"),
                                        fieldWithPath("quantityCount").type(JsonFieldType.NUMBER).description("출석 인정 횟수"),
                                        fieldWithPath("progress").type(JsonFieldType.NUMBER).description("과외 진행률")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("GET 출석부 2 출석 인정")
    void lessonAttendanceCheck() throws Exception {
        Integer quantityCount = 5;

        given(suggestService.teacherChecksAttendance(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(quantityCount);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/attendance/check", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityCount").value(quantityCount))
                .andDo(document("출석부2-출석인정",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("quantityCount").type(JsonFieldType.NUMBER).description("출석 인정 횟수")
                        )
                ));
    }

    @Test
    @DisplayName("GET 출석부 3 출석 인정 취소")
    void lessonAttendanceCancel() throws Exception {
        Integer quantityCount = 5;

        given(suggestService.teacherCancelAttendance(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(quantityCount);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/attendance/cancel", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityCount").value(quantityCount))
                .andDo(document("출석부3-출석인정취소",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("quantityCount").type(JsonFieldType.NUMBER).description("출석 인정 횟수")
                        )
                ));
    }

    @Test
    @DisplayName("GET 결제 영수증 조회")
    void getPaymentReceipt() throws Exception {
        Integer totalCost = 50000;
        Integer quantity = 5;
        String paymentMethod = "카카오페이";
        String name = "김선생";

        GetPaymentReceipt getPaymentInfo = new GetPaymentReceipt(lesson, name, totalCost, quantity, paymentMethod);

        given(suggestDbService.getPaymentReceiptOnMyPage(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/receipt", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.paymentMethod").value(paymentMethod))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(totalCost))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("결제영수증조회",
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
                                getPaymentReceiptResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 선생님용")
    void getTutorSuggest() throws Exception{
        int tabId = 1;

        GetTutorSuggest getTutorSuggest = GetTutorSuggest.builder()
                .suggestId(suggest.getId())
                .days(suggest.getDays())
                .languages(suggest.getLanguages())
                .requests(suggest.getRequests())
                .status(suggest.getSuggestStatus())
                .lessonId(lesson.getId())
                .name(member.getName())
                .build();

        List<GetTutorSuggest> suggestList = new ArrayList<>();
        suggestList.add(getTutorSuggest);

        given(suggestDbService.getTutorSuggestsOnMyPage(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(suggestList));

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/lesson/{lesson-id}/tutor/tab/{tab-id}", 1L, tabId)
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].suggestId").value(suggestList.get(0).getSuggestId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(suggestList.get(0).getLessonId()))
                .andExpect(jsonPath("$.data[0].name").value(suggestList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].days").value(suggestList.get(0).getDays()))
                .andExpect(jsonPath("$.data[0].languages").value(suggestList.get(0).getLanguages()))
                .andExpect(jsonPath("$.data[0].requests").value(suggestList.get(0).getRequests()))
                .andExpect(jsonPath("$.data[0].status").value(suggestList.get(0).getStatus()))
                .andExpect(jsonPath("$.data[0].startTime").value(suggestList.get(0).getStartTime()))
                .andExpect(jsonPath("$.data[0].endTime").value(suggestList.get(0).getEndTime()))
                .andDo(document("신청내역조회(강사)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자"),
                                parameterWithName("tab-id").description("탭 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getTutorSuggestResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 학생용")
    void getStudentSuggest() throws Exception{
        String name = "김선생";
        lesson.addProfileImage(profileImage);

        GetStudentSuggest getStudentSuggest = GetStudentSuggest.builder()
                .suggestId(suggest.getId())
                .lesson(lesson)
                .status(suggest.getSuggestStatus())
                .startTime(suggest.getStartTime())
                .endTime(suggest.getEndTime())
                .name(name)
                .build();

        List<GetStudentSuggest> suggestList = new ArrayList<>();
        suggestList.add(getStudentSuggest);

        given(suggestDbService.getStudentSuggestsOnMyPage(Mockito.anyLong(), Mockito.any()))
                .willReturn(new PageImpl<>(suggestList));

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/student")
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].suggestId").value(suggestList.get(0).getSuggestId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(suggestList.get(0).getLessonId()))
                .andExpect(jsonPath("$.data[0].name").value(suggestList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].title").value(suggestList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].profileImage").value(suggestList.get(0).getProfileImage()))
                .andExpect(jsonPath("$.data[0].company").value(suggestList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(suggestList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].cost").value(suggestList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].status").value(suggestList.get(0).getStatus()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andDo(document("신청내역조회(학생)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getStudentSuggestResponse()
                        )
                ));
    }

}
