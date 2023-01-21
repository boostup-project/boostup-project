package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.dto.GetBookmark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyPageBookmarkGetTest extends BookmarkControllerTest {

    @Test
    @DisplayName("GET 북마크 전체 조회")
    void getBookmarks() throws Exception {
        GetBookmark bookmarkInfo = GetBookmark.builder()
                .bookmark(bookmark)
                .lesson(lesson)
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

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].bookmarkId").value(bookmarkInfo.getBookmarkId()))
                .andExpect(jsonPath("$.data[0].lessonId").value(bookmarkInfo.getLessonId()))
                .andExpect(jsonPath("$.data[0].image").value(bookmarkInfo.getImage()))
                .andExpect(jsonPath("$.data[0].bookmarkUrl").value(bookmarkInfo.getBookmarkUrl()))
                .andExpect(jsonPath("$.data[0].title").value(bookmarkInfo.getTitle()))
                .andExpect(jsonPath("$.data[0].name").value(bookmarkInfo.getName()))
                .andExpect(jsonPath("$.data[0].cost").value(bookmarkInfo.getCost()))
                .andExpect(jsonPath("$.data[0].languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.data[0].address[0]").value("강남구"))
                .andReturn();
    }
}
