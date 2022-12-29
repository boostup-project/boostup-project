package com.codueon.boostUp.domain.review.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewDeleteTest extends ReviewControllerTest {

    @Test
    @DisplayName("리뷰 삭제")
    public void deleteReview() throws Exception {
        Long reviewId = 1L;
        doNothing().when(reviewService).removeReview(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                delete("/review/{review-id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andExpect(status().isNoContent())
                .andReturn();
    }
}
