package com.codueon.boostUp.domain.lesson.controller;


import com.codueon.boostUp.domain.lesson.dto.PatchLessonCurriculum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatchLessonCurriculumTest extends LessonControllerTest {

    @Test
    @DisplayName("과외 커리큘럼 수정")
    void updateCurriculum() throws Exception {
        Long lessonId = 1L;

        PatchLessonCurriculum data = PatchLessonCurriculum.builder()
                .curriculum("수업 4시간")
                .build();

        String content = gson.toJson(data);

        doNothing().when(lessonService).updateCurriculum(Mockito.anyLong(), Mockito.any(PatchLessonCurriculum.class), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        patch("/lesson/{lesson-id}/curriculum/modification", lessonId)
//                                .header(AUTHORIZATION, "Bearer " + accessToken)
//                                .header(Media.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("과외 커리큘럼 수정",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
//                                headerWithName(REFRESH).description("리프레시 토큰")
//                        ),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        requestFields(List.of(
                                        fieldWithPath("curriculum").type(JsonFieldType.STRING).description("과외 커리큘럼")
                                )
                        )
                ));
    }
}