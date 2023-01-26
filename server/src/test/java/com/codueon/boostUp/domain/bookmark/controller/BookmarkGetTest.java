package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkGetTest extends BookmarkControllerTest {

    @Test
    @DisplayName("GET 북마크 여부 조회")
    public void getBookmark() throws Exception {
        Long lessonId = 1L;
        given(bookmarkService.isMemberBookmarked(Mockito.anyLong(), Mockito.anyLong())).willReturn(false);

        ResultActions actions = mockMvc.perform(
                get("/bookmark/lesson/{lesson-id}", lessonId)
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmark").value(false))
                .andDo(document("북마크 여부 조회",
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
                                fieldWithPath("bookmark").type(JsonFieldType.BOOLEAN).description("북마크 여부")
                        )
                ));
    }

    @Test
    @DisplayName("GET 북마크 전체 조회")
    void getBookmarks() throws Exception {
        String tutorName = "김선생";

        GetBookmark bookmarkInfo = GetBookmark.builder()
                .bookmark(bookmark)
                .lesson(lesson)
                .tutorName(tutorName)
                .build();

        List<GetBookmark> bookmarkList = new ArrayList<>();
        bookmarkList.add(bookmarkInfo);

        given(bookmarkService.findBookmarkList(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(bookmarkList));

        ResultActions actions = mockMvc.perform(
                get("/bookmark")
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].bookmarkId").value(bookmarkInfo.getBookmarkId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(bookmarkInfo.getLessonId()))
                .andExpect(jsonPath("$.data[0].image").value(bookmarkInfo.getImage()))
                .andExpect(jsonPath("$.data[0].bookmarkUrl").value(bookmarkInfo.getBookmarkUrl()))
                .andExpect(jsonPath("$.data[0].title").value(bookmarkInfo.getTitle()))
                .andExpect(jsonPath("$.data[0].name").value(bookmarkInfo.getName()))
                .andExpect(jsonPath("$.data[0].cost").value(bookmarkInfo.getCost()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andDo(document("북마크 마이페이지 조회",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("북마크 데이터"),
                                        fieldWithPath("data[].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 식별자"),
                                        fieldWithPath("data[].lessonId").type(JsonFieldType.NUMBER).description("과외 식별자"),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("섬네일 이미지"),
                                        fieldWithPath("data[].bookmarkUrl").type(JsonFieldType.STRING).description("과외 URL"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("과외 타이틀"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("강사 이름"),
                                        fieldWithPath("data[].cost").type(JsonFieldType.NUMBER).description("과외 가격"),
                                        fieldWithPath("data[].languages").type(JsonFieldType.ARRAY).description("과외 가능 언어"),
                                        fieldWithPath("data[].address").type(JsonFieldType.ARRAY).description("과외 가능 주소"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                                )
                        )
                ));
    }
}
