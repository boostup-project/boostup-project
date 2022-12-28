package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.member.service.MemberService;
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

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {MemberController.class})
public class MemberControllerTest {

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberDbService memberDbService;

    protected Member member;

    protected PostMember postMember;

    @BeforeEach
    void setUp() throws Exception {

        postMember = PostMember.builder()
                .email("hdg@gmail.com")
                .password("ghdrlfehd")
                .name("홍길동")
                .build();
    }
}
