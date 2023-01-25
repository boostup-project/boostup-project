package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MemberCreateTest extends MemberControllerTest{

    @Test
    @DisplayName("회원가입 테스트")
    public void createMember() throws Exception {
        doNothing().when(memberService).createMember(Mockito.any(PostMember.class));
        String content = gson.toJson(postMember);

        ResultActions actions =
                mockMvc.perform(
                        post("/member/join")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(content)
                );

        actions
                .andExpect(status().isCreated())
                .andDo(document("회원가입",
                        getRequestPreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("닉네임")
                                )
                        )
                ));
    }
}
