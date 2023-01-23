package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewPatchTest extends ReviewControllerTest {

    @Test
    @DisplayName("리뷰 수정")
    public void editReview() throws Exception {
        Long reviewId = 1L;

        PatchReview patch = PatchReview.builder()
                .score(3)
                .comment("진짜 잘 가르칠 수도 있고 못 가르칠 수도 있습니다.")
                .build();

        String content = gson.toJson(patch);
        doNothing().when(reviewService).editReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(PatchReview.class));

        ResultActions actions = mockMvc.perform(
                patch("/review/{review-id}/edit", reviewId)
                        .header(AUTHORIZATION, BEARER + accessToken)
                        .header(REFRESH_TOKEN, refreshToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andDo(document("리뷰 수정",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("리뷰 점수"),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("리뷰 내용")
                                )
                        )
                ));
    }
}
