
package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.GetLesson;
import com.codueon.boostUp.domain.lesson.dto.GetLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.GetLessonInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetDetailPageLessonTest extends LessonControllerTest {

    @Test
    @DisplayName("과외 상세페이지 요약 정보 조회 테스트")
    void getLessonTest() throws Exception {

        GetLesson getLesson = GetLesson.builder()
                .lesson(lesson)
                .build();

        given(lessonService.getDetailLesson(Mockito.anyLong())).willReturn(getLesson);

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/{lesson-id}", lesson.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.profileImage").value(lesson.getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.languages[0]").value("Java"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.name").value(lesson.getName()))
                .andExpect(jsonPath("$.title").value(lesson.getTitle()))
                .andExpect(jsonPath("$.company").value(lesson.getCompany()))
                .andExpect(jsonPath("$.career").value(lesson.getCareer()))
                .andExpect(jsonPath("$.cost").value(lesson.getCost()))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andReturn();

    }

    @Test
    @DisplayName("과외 상세페이지 상세 정보 조회 테스트")
    void getLessonInfoTest() throws Exception {

        GetLessonInfo getLessonInfo = GetLessonInfo.builder()
                .lessonInfo(lessonInfo)
                .build();

        given(lessonService.getDetailLessonInfo(Mockito.anyLong())).willReturn(getLessonInfo);

        ResultActions actions =
                mockMvc.perform(get("/lesson/{lesson-id}/detailInfo", lesson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.introduction").value(lessonInfo.getIntroduction()))
                .andExpect(jsonPath("$.detailCompany").value(lessonInfo.getCompanies()))
                .andExpect(jsonPath("$.personality").value(lessonInfo.getPersonality()))
                .andExpect(jsonPath("$.detailCost").value(lessonInfo.getCosts()))
                .andExpect(jsonPath("$.detailLocation").value(lessonInfo.getFavoriteLocation()))
                .andReturn();
    }

    @Test
    @DisplayName("과외 상세페이지 커리큘럼 정보 조회 테스트")
    void getCurriculumTest() throws Exception {
        GetLessonCurriculum getLessonCurriculum = GetLessonCurriculum.builder()
                .curriculum(curriculum.getCurriculum())
                .build();

        given(lessonService.getDetailLessonCurriculum(Mockito.anyLong())).willReturn(getLessonCurriculum);

        ResultActions actions =
                mockMvc.perform(get("/lesson/{lesson-id}/curriculum", lesson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.curriculum").value(curriculum.getCurriculum()))
                .andReturn();
    }
}