package com.codueon.boostUp.domain.member.controller;

import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.member.service.MemberService;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
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

    protected DataForTest data = new DataForTest();

    protected Member member;
    protected PostMember postMember;

    @MockBean
    protected RedisUtils redisUtils;
    protected JwtTokenUtils jwtTokenUtils;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = data.getMember1();
        postMember = PostMember.builder()
                .email("hdg@gmail.com")
                .password("ghdrlfehd")
                .name("홍길동")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getName(), null, member.getId());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
