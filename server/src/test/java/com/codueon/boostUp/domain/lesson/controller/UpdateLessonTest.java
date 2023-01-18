package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.PostLessonInfoEdit;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateLessonTest extends LessonControllerTest {

    @Test
    @DisplayName("POST 과외 수정")
    void updateLesson() throws Exception {

        Long lessonId = 1L;

        List<Long> languageList = new ArrayList<>();
        List<Long> addressList = new ArrayList<>();

        languageList.add(2L);
        addressList.add(2L);

        PostLessonInfoEdit data = PostLessonInfoEdit.builder()
                .languages(languageList)
                .title("우아한 형제들 가자!!")
                .company( "카카오페이")
                .career(4)
                .cost(70000)
                .addresses(addressList)
                .build();

        MockMultipartFile profileImage =
                new MockMultipartFile("profileImage", "updateFile.jpg", "image/jpg", "<<jpg data>>".getBytes());

        String content = gson.toJson(data);

        doNothing().when(lessonService).updateLessonInfo(Mockito.any(), Mockito.any(PostLessonInfoEdit.class), Mockito.anyLong(), Mockito.any());

        ResultActions actions =
                mockMvc.perform(
                        multipart("/lesson/{lesson-id}/modification", lessonId)
                                .file(profileImage)
                                .file(new MockMultipartFile("data", "","application/json", content.getBytes(StandardCharsets.UTF_8)))
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
                        "과외 요약 수정",
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
//                                headerWithName(REFRESH).description("리프레시 토큰")
//                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("languages").type(JsonFieldType.ARRAY).description("가능 언어"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                                        fieldWithPath("company").type(JsonFieldType.STRING).description("재직 회사명"),
                                        fieldWithPath("career").type(JsonFieldType.NUMBER).description("경력"),
                                        fieldWithPath("cost").type(JsonFieldType.NUMBER).description("수업료"),
                                        fieldWithPath("addresses").type(JsonFieldType.ARRAY).description("가능 지역")
                                )
                        )
                ));
    }
}