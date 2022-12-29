package com.codueon.boostUp.domain.bookmark.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkCreateTest extends BookmarkControllerTest {

    @Test
    @DisplayName("GET 북마크 생성 - 북마크가 존재하지 않으면 생성하기")
    void changeBookmark() throws Exception {
        Long lessonId = 1L;
        given(bookmarkService.changeBookmarkStatus(Mockito.anyLong(), Mockito.anyLong())).willReturn(true);

        ResultActions actions =
                mockMvc.perform(
                        get("/bookmark/lesson/{lesson-id}/edit", lessonId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andReturn();
    }
}
