package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.reveiw.dto.GetReview;
import com.codueon.boostUp.domain.reveiw.dto.GetReviewMyPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
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

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(4.0))
                .andExpect(jsonPath("$.totalReviews").value(1))
                .andExpect(jsonPath("$.data[0].reviewId").value(review.getId()))
                .andExpect(jsonPath("$.data[0].image").value(image))
                .andExpect(jsonPath("$.data[0].name").value(member.getName()))
                .andExpect(jsonPath("$.data[0].score").value(review.getScore()))
                .andExpect(jsonPath("$.data[0].comment").value(review.getComment()))
                .andExpect(jsonPath("$.data[0].createdAt").value(review.getCreatedAt().toString()))
                .andDo(document("리뷰 상세페이지 조회",
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("리뷰 데이터"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("섬네일 이미지"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("강사 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("리뷰 점수"),
                                        fieldWithPath("data[].comment").type(JsonFieldType.STRING).description("리뷰 내용"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("리뷰 작성 시간"),

                                        fieldWithPath("average").type(JsonFieldType.NUMBER).description("리뷰 평균"),
                                        fieldWithPath("totalReviews").type(JsonFieldType.NUMBER).description("리뷰 개수"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                                )
                        )
                ));
    }

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
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].languages[0]").value(lesson.getLessonLanguages().get(0).getLanguageInfo().getLanguages()))
                .andExpect(jsonPath("$.data[0].address[0]").value(lesson.getLessonAddresses().get(0).getAddressInfo().getAddress()))
                .andExpect(jsonPath("$.data[0].profileImage").value(lesson.getProfileImage().getFilePath()))
                .andExpect(jsonPath("$.data[0].name").value(lesson.getName()))
                .andExpect(jsonPath("$.data[0].title").value(lesson.getTitle()))
                .andExpect(jsonPath("$.data[0].company").value(lesson.getCompany()))
                .andExpect(jsonPath("$.data[0].career").value(lesson.getCareer()))
                .andExpect(jsonPath("$.data[0].cost").value(lesson.getCost()))
                .andExpect(jsonPath("$.data[0].startTime").value(start.toString()))
                .andExpect(jsonPath("$.data[0].endTime").value(end.toString()))
                .andExpect(jsonPath("$.data[0].score").value(review.getScore()))
                .andExpect(jsonPath("$.data[0].comment").value(review.getComment()))
//                .andExpect(jsonPath("$.data[0].createdAt").value(review.getCreatedAt().toString()))
                .andDo(document("리뷰 마이페이지 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("북마크 데이터"),
                                        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("강사 이름"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("과외 타이틀"),
                                        fieldWithPath("data[].company").type(JsonFieldType.STRING).description("강사 대표회사"),
                                        fieldWithPath("data[].career").type(JsonFieldType.NUMBER).description("강사 경력"),
                                        fieldWithPath("data[].cost").type(JsonFieldType.NUMBER).description("과외 가격"),
                                        fieldWithPath("data[].startTime").type(JsonFieldType.STRING).description("과외 시작 날짜"),
                                        fieldWithPath("data[].endTime").type(JsonFieldType.STRING).description("과외 종료 날짜"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("리뷰 점수"),
                                        fieldWithPath("data[].comment").type(JsonFieldType.STRING).description("리뷰 내용"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("리뷰 작성 시간"),
                                        fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                                        fieldWithPath("data[].languages").type(JsonFieldType.ARRAY).description("과외 가능 언어"),
                                        fieldWithPath("data[].address").type(JsonFieldType.ARRAY).description("과외 가능 주소"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("리뷰 작성 날짜"),

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
