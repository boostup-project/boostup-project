package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.PostLesson;
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

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostLessonTest extends LessonControllerTest{
    @Test
    @DisplayName("Post 과외 생성")
    void postLesson() throws Exception {
        List<Integer> laungaueList = new ArrayList<>();
        List<Integer> addressList = new ArrayList<>();
        laungaueList.add(1);
        addressList.add(1);

        PostLesson data = PostLesson.builder()
                .title("자바가 제일 쉬울 수도 있고 어려울 수도 있습니다")
                .company("안우아한 형제들")
                .career(5)
                .cost(50000)
                .languages(laungaueList)
                .address(addressList)
                .introduction("자바 고수가 될 수도 있고, 안될 수도 있습니다.")
                .detailCompany("카카오페이 3년, 안우아한 형제들 2년")
                .detailLocation("무조건 스타벅스")
                .personality("천사입니다.")
                .detailCost("Java 수강생 : 50000원, JS 수강생 : 7,000,000원")
                .curriculum("3시간 수업")
                .build();

        MockMultipartFile profileImage =
                new MockMultipartFile("profileImage", "test.jpg", "image/jpg", "<<jpg data>>".getBytes());
        MockMultipartFile careerImage =
                new MockMultipartFile("careerImage", "test1.jpg", "image/jpg", "<<jpg data>>".getBytes());

        String content = gson.toJson(data);
        doNothing().when(lessonService).createLesson(Mockito.any(PostLesson.class), Mockito.anyLong(), Mockito.any(), Mockito.anyList());

        ResultActions actions =
                mockMvc.perform(
                        multipart("/lesson/registration")
                                .file(profileImage)
                                .file(careerImage)
                                .file(new MockMultipartFile("data","", "application/json", content.getBytes(StandardCharsets.UTF_8) ))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .with(csrf())
                                .content(content)

                );
        actions
                .andExpect(status().isCreated())
                .andDo(document(
                        "과외 등록",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                                        fieldWithPath("company").type(JsonFieldType.STRING).description("재직 회사명"),
                                        fieldWithPath("career").type(JsonFieldType.NUMBER).description("경력"),
                                        fieldWithPath("cost").type(JsonFieldType.NUMBER).description("수업료"),
                                        fieldWithPath("languages").type(JsonFieldType.ARRAY).description("가능 언어"),
                                        fieldWithPath("address").type(JsonFieldType.ARRAY).description("가능 지역"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("한줄 소개"),
                                        fieldWithPath("detailCompany").type(JsonFieldType.STRING).description("추가 정보 경력"),
                                        fieldWithPath("detailLocation").type(JsonFieldType.STRING).description("장소 세부사항"),
                                        fieldWithPath("personality").type(JsonFieldType.STRING).description("성격"),
                                        fieldWithPath("detailCost").type(JsonFieldType.STRING).description("수강료 세부사항"),
                                        fieldWithPath("curriculum").type(JsonFieldType.STRING).description("커리큘럼")
                                )
                        )
                ));
    }
}
