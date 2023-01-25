package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.PostPaymentUrl;
import com.codueon.boostUp.domain.suggest.dto.PostReason;
import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class SuggestPostTest extends SuggestControllerTest{

    @Test
    @DisplayName("POST 신청 프로세스 1 - 과외 신청")
    void createSuggest() throws Exception{
        Long lessonId = 1L;

        PostSuggest post = PostSuggest.builder()
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .build();

        String content = gson.toJson(post);
        doNothing().when(suggestService).createSuggest(Mockito.any(), Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        post("/suggest/lesson/{lesson-id}", lessonId)
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())

                );

        actions
                .andExpect(status().isCreated())
                .andDo(document("신청1-과외신청",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("lesson-id").description("과외 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("days").type(JsonFieldType.STRING).description("희망 요일"),
                                        fieldWithPath("languages").type(JsonFieldType.STRING).description("희망 언어"),
                                        fieldWithPath("requests").type(JsonFieldType.STRING).description("요청 사항")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("POST 신청 프로세스 2-1 수락하기")
    void acceptSuggest() throws Exception{
        PostPaymentUrl post = PostPaymentUrl.builder()
                .quantity(1)
                .build();

        String content = gson.toJson(post);
        doNothing().when(suggestService).acceptSuggest(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt());

        ResultActions actions =
                mockMvc.perform(
                        post("/suggest/{suggest-id}/accept", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청2.1-신청수락",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("과외 횟수")
                        )
                ));
    }

    @Test
    @DisplayName("POST 신청 프로세스 2-2 거절하기")
    void declineSuggest() throws Exception {
        PostReason postReason = PostReason.builder()
                .reason("배울 자세가 안 되어있음 쯧")
                .build();

        String content = gson.toJson(postReason);
        doNothing().when(suggestService).declineSuggest(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());

        ResultActions actions =
                mockMvc.perform(
                        post("/suggest/{suggest-id}/decline", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("신청2.2-신청거절",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        requestFields(
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("거절 사유")
                        )
                ));
    }

}
