package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestGetTest extends SuggestControllerTest{

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 선생님용")
    void getTutorSuggest() throws Exception{

        int tabId = 1;

        Lesson lesson = Lesson.builder()
                .id(1L)
                .name("길동씨")
                .build();

        List<GetTutorSuggest> suggestList = new ArrayList<>();
        suggestList.add(new GetTutorSuggest(suggest, lesson.getId(), lesson.getName()));

        given(suggestDbService.getTutorSuggestsOnMyPage(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(suggestList));

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/{lesson-id}/suggest/tutor/tab/{tab-id}", 1L, tabId)
                );

        actions.andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("GET 마이페이지 신청 내역 조회 - 학생용")
    void getStudentSuggest() throws Exception{

        ProfileImage profileImage = ProfileImage.builder().filePath("gddong.jpg").build();

        Lesson lesson = Lesson.builder()
                .title("과외하는 길동씨")
                .company("네카라쿠배")
                .cost(5000)
                .id(1L)
                .name("김길동")
                .build();

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

    @Test
    @DisplayName("GET 결제 페이지 조회")
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

        given(suggestService.getSuggestInfo(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getSuggestInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/{lesson-id}/suggest/{suggest-id}/suggest-info", lesson.getId(), suggest.getLessonId())
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }
}
