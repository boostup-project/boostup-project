package com.codueon.boostUp.domain.bookmark.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkDeleteTest extends BookmarkControllerTest {

    @Test
    @DisplayName("GET 북마크 삭제 - 북마크가 존재하면 삭제하기")
    void deleteBookmark() throws Exception {
        Long lessonId = 1L;
        given(bookmarkService.changeBookmarkStatus(Mockito.anyLong(), Mockito.anyLong())).willReturn(false);

        ResultActions actions =
                mockMvc.perform(
                        get("/bookmark/lesson/{lesson-id}/edit", lessonId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmark").value(false))
                .andReturn();
    }
}
