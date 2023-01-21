package com.codueon.boostUp.domain.lesson.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetMyPageLessonTest extends LessonControllerTest {

    @Test
    @DisplayName("마이페이지 선생님 자기 과외 정보 조회 테스트")
    void getLessonMypage() throws Exception {
        String url = "http://localhost:3000/lesson/tutor";

        given(lessonService.getLessonMypage(Mockito.anyLong()))
                .willReturn(url);

        ResultActions actions =
                mockMvc.perform(get("/lesson/tutor")
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()));

        actions.andExpect(status().isOk())
                .andReturn();
    }
}
