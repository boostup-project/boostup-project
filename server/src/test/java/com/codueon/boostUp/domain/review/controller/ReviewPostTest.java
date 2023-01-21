package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.reveiw.dto.PostReview;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewPostTest extends ReviewControllerTest {

    @Test
    @DisplayName("사용자 리뷰 생성 테스트")
    public void createReview() throws Exception {
        PostReview post = PostReview.builder()
                .score(4)
                .comment("과외가 끔찍했어요!")
                .build();
        String content = gson.toJson(post);
        Long lessonId = 1L;
        Long suggestId = 1L;

        doNothing().when(reviewService).createStudentReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(PostReview.class));

        ResultActions actions =
                mockMvc.perform(post("/review/lesson/{lesson-id}/suggest/{suggest-id}", lessonId, suggestId)
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
                );

        actions.andExpect(status().isCreated())
                .andReturn();
    }
}
