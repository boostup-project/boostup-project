package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import com.codueon.boostUp.global.exception.GlobalAdvice;
import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {SuggestController.class})
class SuggestControllerTest {

    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GlobalAdvice globalAdvice;

    @MockBean
    protected SuggestService suggestService;

    @MockBean
    protected SuggestDbService suggestDbService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected Member member;
    protected Suggest suggest;

    @BeforeEach
    void setUp() throws Exception {

        member = Member.builder()
                .id(1L)
                .name("김길동")
                .email("gddong@gmail.com")
                .roles(List.of("USER"))
                .build();

        suggest = Suggest.builder()
                .id(1L)
                .lessonId(1L)
                .memberId(1L)
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .build();

    }
}