package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.vo.AuthInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SuggestDeleteTest extends SuggestControllerTest{

    @Test
    @DisplayName("DELETE 과외 신청 취소")
    void cancelSuggest() throws Exception{
        doNothing().when(suggestService).cancelSuggest(Mockito.anyLong(), Mockito.any(AuthInfo.class));

        ResultActions actions =
                mockMvc.perform(
                        delete("/suggest/{suggest-id}", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .with(csrf())
                );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("신청취소",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("DELETE 강사 종료 과외 삭제")
    void deleteTutorEndOfSuggest() throws Exception{
        doNothing().when(suggestService).deleteTutorEndOfSuggest(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/suggest/{suggest-id}/tutor", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .with(csrf())
                );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("강사종료과외삭제",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("DELETE 강사 종료 과외 삭제")
    void deleteStudentEndOfSuggest() throws Exception{
        doNothing().when(suggestService).deleteStudentEndOfSuggest(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/suggest/{suggest-id}/student", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .with(csrf())
                );

        actions
                .andExpect(status().isNoContent())
                .andDo(document("학생종료과외삭제",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }
}
