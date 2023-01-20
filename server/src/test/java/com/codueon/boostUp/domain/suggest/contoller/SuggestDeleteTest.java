package com.codueon.boostUp.domain.suggest.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;


public class SuggestDeleteTest extends SuggestControllerTest{

    @Test
    @DisplayName("DELETE 과외 신청 취소")
    void cancelSuggest() throws Exception{
        doNothing().when(suggestService).cancelSuggest(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/suggest/{suggest-id}", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                                .with(csrf())
                );

        actions.andExpect(status().isNoContent())
                .andDo(document("신청취소",
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        )
                ));
    }
}
