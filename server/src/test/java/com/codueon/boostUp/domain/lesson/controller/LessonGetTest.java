package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.Get.GetLesson;
import com.codueon.boostUp.domain.lesson.dto.Get.GetLessonCurriculum;
import com.codueon.boostUp.domain.lesson.dto.Get.GetLessonInfo;
import com.codueon.boostUp.domain.lesson.dto.Post.PostSearchLesson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LessonGetTest extends LessonControllerTest {

    @Test
    @DisplayName("메인페이지 과외 상세 검색 테스트")
    void getDetailSearchForLessonTest() throws Exception {

        PostSearchLesson postSearchLesson = PostSearchLesson.builder()
                .name("김길동")
                .address(1)
                .career(5)
                .language(1)
                .startCost(2000)
                .endCost(8000)
                .build();

        String content = gson.toJson(postSearchLesson);

        given(lessonService.getDetailSearchLessons(Mockito.anyLong(), Mockito.any(PostSearchLesson.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse));

        ResultActions actions =
                mockMvc.perform(post("/lesson/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))

                .andDo(document("과외 상세 검색",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("address").type(JsonFieldType.NUMBER).description("검색 주소"),
                                        fieldWithPath("career").type(JsonFieldType.NUMBER).description("최소 경력"),
                                        fieldWithPath("language").type(JsonFieldType.NUMBER).description("검색 언어"),
                                        fieldWithPath("startCost").type(JsonFieldType.NUMBER).description("최소 가격"),
                                        fieldWithPath("endCost").type(JsonFieldType.NUMBER).description("최대 가격")
                                )
                        ),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("메인페이지 과외 상세 검색 테스트(로그인 시)")
    void getDetailSearchForLessonTest2() throws Exception {
        PostSearchLesson postSearchLesson = PostSearchLesson.builder()
                .name("김길동")
                .address(1)
                .career(5)
                .language(1)
                .startCost(2000)
                .endCost(8000)
                .build();

        String content = gson.toJson(postSearchLesson);

        given(lessonService.getDetailSearchLessons(Mockito.anyLong(), Mockito.any(PostSearchLesson.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(post("/lesson/search")
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))

                .andDo(document("과외 상세 검색(로그인)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("address").type(JsonFieldType.NUMBER).description("검색 주소"),
                                        fieldWithPath("career").type(JsonFieldType.NUMBER).description("최소 경력"),
                                        fieldWithPath("language").type(JsonFieldType.NUMBER).description("검색 언어"),
                                        fieldWithPath("startCost").type(JsonFieldType.NUMBER).description("최소 가격"),
                                        fieldWithPath("endCost").type(JsonFieldType.NUMBER).description("최대 가격")
                                )
                        ),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("메인페이지 과외 전체 조회 테스트")
    void getMainPageLessonInfoTest1() throws Exception {
        given(lessonService.getMainPageLessons(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse));

        ResultActions actions =
                mockMvc.perform(get("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))

                .andDo(document("과외 전체 조회",
                        getResponsePreProcessor(),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("메인페이지 과외 전체 조회 테스트 (로그인 시)")
    void getMainPageLessonInfoTest2() throws Exception {
        given(lessonService.getMainPageLessons(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(get("/lesson")
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))

                .andDo(document("과외 전체 조회(로그인)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("메인페이지 언어별 과외 조회 테스트")
    void getMainPageForLanguageTest() throws Exception {
        Long languageId = 1L;

        given(lessonService.getMainPageLessonsAboutLanguage(Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse));

        ResultActions actions =
                mockMvc.perform(get("/lesson/language/{language-id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))

                .andDo(document("과외 언어별 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("language-id").description("언어 식별자")
                        ),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("메인페이지 언어별 과외 조회 테스트(로그인 시)")
    void getMainPageForLanguageTest2() throws Exception {
        Long languageId = 1L;

        given(lessonService.getMainPageLessonsAboutLanguage(Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(get("/lesson/language/{language-id}", languageId)
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].ticketId").value(1L))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].ticketId").value(1L))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Go"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))

                .andDo(document("과외 언어별 조회(로그인)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("language-id").description("언어 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getLessonPageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("과외 상세페이지 요약 정보 조회 테스트")
    void getLessonTest() throws Exception {
        GetLesson getLesson = GetLesson.builder()
                .name(member.getName())
                .lesson(lesson)
                .build();
        getLesson.setEditable(true);

        given(lessonService.getDetailLesson(Mockito.anyLong(), Mockito.anyLong())).willReturn(getLesson);

        ResultActions actions =
                mockMvc.perform(
                        get("/lesson/{lesson-id}", lesson.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileImage").value(lesson.getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.name").value(data.getMember1().getName()))
                .andExpect(jsonPath("$.title").value(lesson.getTitle()))
                .andExpect(jsonPath("$.company").value(lesson.getCompany()))
                .andExpect(jsonPath("$.career").value(lesson.getCareer()))
                .andExpect(jsonPath("$.cost").value(lesson.getCost()))
                .andExpect(jsonPath("$.editable").value(getLesson.getEditable()))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("과외 언어별 조회(로그인)",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("강사 이름"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                                        fieldWithPath("company").type(JsonFieldType.STRING).description("강사 대표회사"),
                                        fieldWithPath("career").type(JsonFieldType.NUMBER).description("강사 경력"),
                                        fieldWithPath("cost").type(JsonFieldType.NUMBER).description("과외 가격"),
                                        fieldWithPath("editable").type(JsonFieldType.BOOLEAN).description("수정 가능 여부"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                                        fieldWithPath("languages").type(JsonFieldType.ARRAY).description("과외 가능 언어"),
                                        fieldWithPath("address").type(JsonFieldType.ARRAY).description("과외 가능 지역")
                                )
                        )
                ));
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
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.introduction").value(lessonInfo.getIntroduction()))
                .andExpect(jsonPath("$.detailCompany").value(lessonInfo.getCompanies()))
                .andExpect(jsonPath("$.personality").value(lessonInfo.getPersonality()))
                .andExpect(jsonPath("$.detailCost").value(lessonInfo.getCosts()))
                .andExpect(jsonPath("$.detailLocation").value(lessonInfo.getFavoriteLocation()))
                .andExpect(jsonPath("$.careerImages[0].careerImageId").value(1))
                .andExpect(jsonPath("$.careerImages[0].filePath").value("https://test.com/careerImage/test1.jpg"))
                .andExpect(jsonPath("$.careerImages[1].careerImageId").value(2))
                .andExpect(jsonPath("$.careerImages[1].filePath").value("https://test.com/careerImage/test2.jpg"))
                .andExpect(jsonPath("$.careerImages[2].careerImageId").value(3))
                .andExpect(jsonPath("$.careerImages[2].filePath").value("https://test.com/careerImage/test3.jpg"))
                .andDo(document("과외 상세 정보 조회",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("한줄 소개"),
                                        fieldWithPath("detailCompany").type(JsonFieldType.STRING).description("강사 경력"),
                                        fieldWithPath("personality").type(JsonFieldType.STRING).description("강사 성격"),
                                        fieldWithPath("detailCost").type(JsonFieldType.STRING).description("상세 가격"),
                                        fieldWithPath("detailLocation").type(JsonFieldType.STRING).description("상세 지역"),

                                        fieldWithPath("careerImages").type(JsonFieldType.ARRAY).description("경력 이미지"),
                                        fieldWithPath("careerImages[].careerImageId").type(JsonFieldType.NUMBER).description("경력 이미지 식별자"),
                                        fieldWithPath("careerImages[].filePath").type(JsonFieldType.STRING).description("경력 이미지 주소")
                                )
                        )
                ));
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
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curriculum").value(curriculum.getCurriculum()))
                .andDo(document("과외 커리큘럼 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("curriculum").type(JsonFieldType.STRING).description("커리큘럼")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("마이페이지 선생님 자기 과외 URL 조회")
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

        actions
                .andExpect(status().isOk())
                .andDo(document("과외 이동 URL",
                        getRequestPreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }
}