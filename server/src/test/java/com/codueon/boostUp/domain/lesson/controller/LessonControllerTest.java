package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.lesson.service.LessonService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
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
@AutoConfigureWebMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {LessonController.class})
public class LessonControllerTest {

    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected LessonService lessonService;

    @MockBean
    protected LessonDbService lessonDbService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected DataForTest data = new DataForTest();

    protected Member member;
    protected Lesson lesson;
    protected List<Lesson> lessonList;
    protected LessonInfo lessonInfo;
    protected Curriculum curriculum;
    protected List<GetMainPageLesson> mainPageResponse;
    protected List<GetMainPageLesson> mainPageResponse2;

    @MockBean
    protected RedisUtils redisUtils;
    protected JwtTokenUtils jwtTokenUtils;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = data.getMember1();
        lesson = data.getLesson1();
        lessonList = data.getLessonList();
        lessonInfo = data.getLessonInfo1();
        curriculum = data.getCurriculum1();
        mainPageResponse = lessonList.stream()
                .map(lessons -> GetMainPageLesson.builder()
                        .lesson(lessons)
                        .bookmark(false)
                        .build())
                .collect(Collectors.toList());

        mainPageResponse2 = lessonList.stream()
                .map(lessons -> GetMainPageLesson.builder()
                        .lesson(lessons)
                        .bookmark(true)
                        .build())
                .collect(Collectors.toList());

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getName(), null, member.getId());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
