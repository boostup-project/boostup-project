package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.GetStudentSuggest;
import com.codueon.boostUp.domain.suggest.dto.GetSuggestInfo;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestGetTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 신청 프로세스 3 결제 페이지 조회")
    void getSuggestInfo() throws Exception {

        Integer quantity = 5;
        Integer totalCost = 250000;

        lesson.addProfileImage(profileImage);

        GetSuggestInfo getSuggestInfo =
                new GetSuggestInfo(lesson, totalCost, quantity);

        given(suggestService.getSuggestInfo(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getSuggestInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/suggest-info", suggest.getLessonId())
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
                        get("/suggest/{suggest-id}/done", suggest.getId())
                );

        actions.andExpect(status().isOk())
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
                .andReturn();
    }

}
