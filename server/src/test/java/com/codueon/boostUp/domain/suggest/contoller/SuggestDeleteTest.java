package com.codueon.boostUp.domain.suggest.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SuggestDeleteTest extends SuggestControllerTest{

    @Test
    @DisplayName("DELETE 과외 신청 취소")
    void cancelSuggest() throws Exception{

        doNothing().when(suggestDbService).cancelSuggest(Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/suggest/{suggest-id}", suggest.getId())
                                .with(csrf())
                );

        actions.andExpect(status().isNoContent())
                .andReturn();
    }
}
