package com.codueon.boostUp.domain.bookmark.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkGetTest extends BookmarkControllerTest {

    @Test
    @DisplayName("북마크 여부 조회")
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

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmark").value(false))
                .andReturn();
    }
}
