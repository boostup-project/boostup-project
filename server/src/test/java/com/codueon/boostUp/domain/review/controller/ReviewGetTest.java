package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewGetTest extends ReviewControllerTest {

    @Test
    @DisplayName("상세페이지 리뷰 전체 조회 메서드")
    public void getReviews() throws Exception {

        String image = "https://test.test.com/image/test.png";
        Long lessonId = 1L;

        GetReview reviewRes = GetReview.builder()
                .review(review)
                .image(image)
                .name(member.getName())
                .build();

        List<GetReview> reviewList = new ArrayList<>();
        reviewList.add(reviewRes);
        given(reviewService.findAllDetailInfoReviews(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(reviewList));

        ResultActions actions =
                mockMvc.perform(
                        get("/review/lesson/{lesson-id}", lessonId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(4.0))
                .andExpect(jsonPath("$.totalReviews").value(1))
                .andExpect(jsonPath("$.data[0].reviewId").value(review.getId()))
                .andExpect(jsonPath("$.data[0].image").value(image))
                .andExpect(jsonPath("$.data[0].name").value(member.getName()))
                .andExpect(jsonPath("$.data[0].score").value(review.getScore()))
                .andExpect(jsonPath("$.data[0].comment").value(review.getComment()))
                .andExpect(jsonPath("$.data[0].createdAt").value(review.getCreatedAt()))
                .andReturn();

    }
}
