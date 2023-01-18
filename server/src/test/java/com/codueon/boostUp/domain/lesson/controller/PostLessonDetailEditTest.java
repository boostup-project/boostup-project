package com.codueon.boostUp.domain.lesson.controller;


import com.codueon.boostUp.domain.lesson.dto.PostLessonDetailEdit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostLessonDetailEditTest extends LessonControllerTest {

    @Test
    @DisplayName("과외 상세 수정")
    void updateLessonDetail() throws Exception {

        Long lessonId = 1L;

        PostLessonDetailEdit data = PostLessonDetailEdit.builder()
                .introduction("코틀린 강의~")
                .detailCompany("12번가 2년, 바닐라페이 3년")
                .detailCost("Java 수강생 70,000원, JS 수강생 999,999,999원")
                .personality("귀엽습니다.")
                .detailLocation("우리집으로 가자")
                .build();

        MockMultipartFile careerImage =
                new MockMultipartFile("careerImage", "updateFile.jpg", "image/jpg", "<<jpg data>>" .getBytes());

        String content = gson.toJson(data);

        doNothing().when(lessonService).updateLessonDetail(Mockito.anyLong(), Mockito.any(PostLessonDetailEdit.class), Mockito.anyLong(), Mockito.anyList());

        ResultActions actions =
                mockMvc.perform(
                        multipart("/lesson/{lesson-id}/detailInfo/modification", lessonId)
                                .file(careerImage)
                                .file(new MockMultipartFile("data", "", "application/json", content.getBytes(StandardCharsets.UTF_8)))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
//                                .header(AUTHORIZATION, "Bearer " + accessToken)
//                                .header(REFRESH, refreshToken)
                                .with(csrf())
                );
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "과외 상세 수정",
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
//                                headerWithName(REFRESH).description("리프레시 토큰")
//                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("한줄 소개"),
                                        fieldWithPath("detailCompany").type(JsonFieldType.STRING).description("재직 회사명"),
                                        fieldWithPath("detailCost").type(JsonFieldType.STRING).description("수업료"),
                                        fieldWithPath("personality").type(JsonFieldType.STRING).description("성격"),
                                        fieldWithPath("detailLocation").type(JsonFieldType.STRING).description("장소 세부사항")
                                )
                        )
                ));
    }
}