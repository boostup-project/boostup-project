package com.codueon.boostUp.domain.lesson.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteLessonTest extends LessonControllerTest{

    @Test
    @DisplayName("괴외 삭제")
    void deleteLesson() throws Exception {

        Long lessonId = 1L;
        doNothing().when(lessonService).deleteLesson(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/lesson/{lesson-id}", lessonId)
//                                .header(AUTHORIZATION, "Bearer " + accessToken)
//                                .header(REFRESH, refreshToken)
                                .with(csrf())
                );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("과외 삭제",
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION).descrption("엑세스 토큰"),
//                                headerWithName(REFRESH).description("리프레시 토큰")
//                        ),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        )
                        ));
    }
}