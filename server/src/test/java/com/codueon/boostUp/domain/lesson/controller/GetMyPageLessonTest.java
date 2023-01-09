package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.GetTutorLesson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetMyPageLessonTest extends SearchControllerTest {

    @Test
    @DisplayName("마이페이지 선생님 자기 과외 정보 조회 테스트")
    void getMyPageMyClassInfoTest() throws Exception {
        GetTutorLesson getTutorLesson = GetTutorLesson.builder()
                .lesson(lesson)
                .build();

        given(searchService.getMyLesson(Mockito.anyLong()))
                .willReturn(getTutorLesson);

        ResultActions actions =
                mockMvc.perform(get("/lesson/tutor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(lesson.getId()))
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
}
