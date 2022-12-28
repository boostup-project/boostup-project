package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyPageReviewGetTest extends ReviewControllerTest {

    @Test
    @DisplayName("사용자 작성 리뷰 조회")
    public void getMyPageReview() throws Exception {

        LocalDateTime start = LocalDateTime.of(2022, 12, 28, 12, 30, 5);
        LocalDateTime end = LocalDateTime.of(2022, 12, 30, 12, 30, 5);

        GetReviewMyPage reviewMyPage = GetReviewMyPage.builder()
                .review(review)
                .lesson(lesson)
                .startTime(start)
                .endTime(end)
                .build();

        List<GetReviewMyPage> reviewMyPageList = new ArrayList<>();
        reviewMyPageList.add(reviewMyPage);

        given(reviewService.findAllMyPageReviews(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(reviewMyPageList));

        ResultActions actions = mockMvc.perform(
                get("/review")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].profileImage").value(lesson.getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].languages[0]").value(lesson.getLessonLanguages().get(0).getLanguages().getLanguages()))
                .andExpect(jsonPath("$.data[0].name").value(lesson.getName()))
                .andExpect(jsonPath("$.data[0].title").value(lesson.getTitle()))
                .andExpect(jsonPath("$.data[0].company").value(lesson.getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lesson.getCareer()))
                .andExpect(jsonPath("$.data[0].cost").value(lesson.getCost()))
                .andExpect(jsonPath("$.data[0].address[0]").value(lesson.getLessonAddresses().get(0).getAddress().getAddress()))
                .andExpect(jsonPath("$.data[0].startTime").value(start.toString()))
                .andExpect(jsonPath("$.data[0].endTime").value(end.toString()))
                .andExpect(jsonPath("$.data[0].score").value(review.getScore()))
                .andExpect(jsonPath("$.data[0].createdAt").value(review.getCreatedAt()))
                .andReturn();

    }
}
