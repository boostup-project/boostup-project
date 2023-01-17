package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.GetPaymentReceipt;
import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentInfo;
import com.codueon.boostUp.domain.suggest.dto.GetTutorSuggest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestGetTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 3 결제 페이지 조회")
    void getPaymentInfo() throws Exception {

        Integer quantity = 5;
        Integer totalCost = 250000;

        lesson.addProfileImage(profileImage);

        GetPaymentInfo getPaymentInfo =
                new GetPaymentInfo(lesson, totalCost, quantity);

        given(suggestService.getPaymentInfo(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/suggest-info", suggest.getLessonId())
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(totalCost))
                .andExpect(jsonPath("$.languages[0]").value("Java"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andReturn();
    }

    @Test
    @DisplayName("GET 신청 프로세스 8 과외 종료")
    void endOfLesson() throws Exception {
        doNothing().when(suggestService).setSuggestStatusAndEndTime(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/done", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("GET 결제 영수증 조회")
    void getPaymentReceipt() throws Exception {
        Integer totalCost = 50000;
        Integer quantity = 5;
        String paymentMethod = "카카오페이";

        GetPaymentReceipt getPaymentInfo = new GetPaymentReceipt(lesson, totalCost, quantity, paymentMethod);

        given(suggestService.getPaymentReceipt(suggest.getId(), member.getId()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/receipt", suggest.getId())
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.paymentMethod").value(paymentMethod))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(totalCost))
                .andExpect(jsonPath("$.languages[0]").value("Java"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andReturn();
    }

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 선생님용")
    void getTutorSuggest() throws Exception{
        int tabId = 1;

        List<GetTutorSuggest> suggestList = new ArrayList<>();
        suggestList.add(new GetTutorSuggest(suggest, lesson.getId(), lesson.getName()));

        given(suggestDbService.getTutorSuggestsOnMyPage(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(suggestList));

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/lesson/{lesson-id}/tutor/tab/{tab-id}", 1L, tabId)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].suggestId").value(suggestList.get(0).getSuggestId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(suggestList.get(0).getLessonId()))
                .andExpect(jsonPath("$.data[0].name").value(suggestList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].days").value(suggestList.get(0).getDays()))
                .andExpect(jsonPath("$.data[0].languages").value(suggestList.get(0).getLanguages()))
                .andExpect(jsonPath("$.data[0].requests").value(suggestList.get(0).getRequests()))
                .andExpect(jsonPath("$.data[0].status").value(suggestList.get(0).getStatus()))
                .andExpect(jsonPath("$.data[0].startTime").value(suggestList.get(0).getStartTime()))
                .andExpect(jsonPath("$.data[0].endTime").value(suggestList.get(0).getEndTime()))
                .andReturn();
    }

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 학생용")
    void getStudentSuggest() throws Exception{
        lesson.addProfileImage(profileImage);

        List<GetStudentSuggest> suggestList = new ArrayList<>();
        suggestList.add(new GetStudentSuggest(suggest, lesson));

        given(suggestDbService.getStudentSuggestsOnMyPage(Mockito.anyLong(), Mockito.any()))
                .willReturn(new PageImpl<>(suggestList));

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/student")
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].suggestId").value(suggestList.get(0).getSuggestId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(suggestList.get(0).getLessonId()))
                .andExpect(jsonPath("$.data[0].name").value(suggestList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].title").value(suggestList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].profileImage").value(suggestList.get(0).getProfileImage()))
                .andExpect(jsonPath("$.data[0].company").value(suggestList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(suggestList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].cost").value(suggestList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].status").value(suggestList.get(0).getStatus()))
                .andExpect(jsonPath("$.data[0].startTime").value(suggestList.get(0).getStartTime()))
                .andExpect(jsonPath("$.data[0].endTime").value(suggestList.get(0).getEndTime()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andReturn();
    }

}
