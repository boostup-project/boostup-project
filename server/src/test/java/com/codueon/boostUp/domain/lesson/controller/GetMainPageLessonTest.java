package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.PostSearchLesson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetMainPageLessonTest extends LessonControllerTest {

    @Test
    @DisplayName("메인페이지 과외 상세 검색 테스트")
    void getDetailSearchForLessonTest() throws Exception {
        PostSearchLesson postSearchLesson = PostSearchLesson.builder()
                .address(1L)
                .career(5)
                .language(1L)
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
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))
                .andReturn();
    }

    @Test
    @DisplayName("메인페이지 과외 상세 검색 테스트(로그인 시)")
    void getDetailSearchForLessonTest2() throws Exception {
        PostSearchLesson postSearchLesson = PostSearchLesson.builder()
                .address(1L)
                .career(5)
                .language(1L)
                .startCost(2000)
                .endCost(8000)
                .build();

        String content = gson.toJson(postSearchLesson);

        given(lessonService.getDetailSearchLessons(Mockito.anyLong(), Mockito.any(PostSearchLesson.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(post("/lesson/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))
                .andReturn();
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
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))
                .andReturn();
    }

    @Test
    @DisplayName("메인페이지 과외 전체 조회 테스트 (로그인 시)")
    void getMainPageLessonInfoTest2() throws Exception {

        given(lessonService.getMainPageLessons(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(get("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))
                .andReturn();
    }

    @Test
    @DisplayName("메인페이지 언어별 과외 조회 테스트")
    void getMainPageForLanguageTest() throws Exception {
        Long languageId = 1L;

        given(lessonService.getMainPageLessonsAboutLanguage(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse));

        ResultActions actions =
                mockMvc.perform(get("/lesson/language/{language-id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(false))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(false))
                .andReturn();
    }

    @Test
    @DisplayName("메인페이지 언어별 과외 조회 테스트(로그인 시)")
    void getMainPageForLanguageTest2() throws Exception {
        Long languageId = 1L;

        given(lessonService.getMainPageLessonsAboutLanguage(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(mainPageResponse2));

        ResultActions actions =
                mockMvc.perform(get("/lesson/language/{language-id}", languageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lessonId").value(lessonList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[0].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[0].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].title").value(lessonList.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].cost").value(lessonList.get(0).getCost()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lessonList.get(0).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lessonList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].company").value(lessonList.get(0).getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lessonList.get(0).getCareer()))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[0].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[0].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[0].bookmark").value(true))

                .andExpect(jsonPath("$.data[1].lessonId").value(lessonList.get(1).getId()))
                .andExpect(jsonPath("$.data[1].languages[0]").value("Java"))
                .andExpect(jsonPath("$.data[1].languages[1]").value("Python"))
                .andExpect(jsonPath("$.data[1].languages[2]").value("Javascript"))
                .andExpect(jsonPath("$.data[1].title").value(lessonList.get(1).getTitle()))
                .andExpect(jsonPath("$.data[1].cost").value(lessonList.get(1).getCost()))
                .andExpect(jsonPath("$.data[1].profileImage").value(lessonList.get(1).getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[1].name").value(lessonList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].company").value(lessonList.get(1).getCompany()))
                .andExpect(jsonPath("$.data[1].career").value(lessonList.get(1).getCareer()))
                .andExpect(jsonPath("$.data[1].address[0]").value("강남구"))
                .andExpect(jsonPath("$.data[1].address[1]").value("강동구"))
                .andExpect(jsonPath("$.data[1].address[2]").value("강북구"))
                .andExpect(jsonPath("$.data[1].bookmark").value(true))
                .andReturn();
    }
}