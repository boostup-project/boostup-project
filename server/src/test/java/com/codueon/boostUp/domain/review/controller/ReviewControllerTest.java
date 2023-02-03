package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.reveiw.controller.ReviewController;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.reveiw.service.ReviewDbService;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(controllers = {ReviewController.class})
public class ReviewControllerTest {
    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected ReviewDbService reviewDbService;

    @MockBean
    protected SuggestDbService suggestDbService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected Member member;
    protected Review review;
    protected Lesson lesson;
    protected Suggest suggest;

    @MockBean
    protected RedisUtils redisUtils;
    protected JwtTokenUtils jwtTokenUtils;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE, objectMapper);

        member = Member.builder()
                .id(1L)
                .name("김길동")
                .email("gddong@gmail.com")
                .roles(List.of("USER"))
                .build();

        lesson = Lesson.builder()
                .id(1L)
                .title("자바 숙성 강의")
                .cost(5000)
                .memberId(1L)
                .company("배달의 민족")
                .career(100)
                .build();

        LessonLanguage lessonLanguage = LessonLanguage.builder()
                .id(1L)
                .languageId(1)
                .build();

        LessonAddress lessonAddress = LessonAddress.builder()
                .id(1L)
                .addressId(1)
                .build();

        ProfileImage profileImage = ProfileImage.builder()
                .filePath("https://test.com/image/test.png")
                .build();

        lesson.addLessonLanguage(lessonLanguage);
        lesson.addLessonAddress(lessonAddress);
        lesson.addProfileImage(profileImage);

        review = Review.builder()
                .id(1L)
                .score(4)
                .comment("과외가 끔찍했어요!")
                .memberId(1L)
                .lessonId(1L)
                .build();

        suggest = Suggest.builder()
                .id(1L)
                .lessonId(1L)
                .memberId(1L)
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = JwtAuthenticationToken.builder()
                .credential(null)
                .id(member.getId())
                .name(member.getName())
                .principal(member.getEmail())
                .authorities(authorities)
                .accessToken(accessToken)
                .isExpired(false)
                .build();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
