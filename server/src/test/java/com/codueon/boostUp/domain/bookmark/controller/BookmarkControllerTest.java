package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.LessonAddress;
import com.codueon.boostUp.domain.lesson.entity.LessonLanguage;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.member.entity.Member;
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
@WebMvcTest(controllers = {BookmarkController.class})
public class BookmarkControllerTest {
    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected Member member;
    protected Lesson lesson;
    protected Bookmark bookmark;

    @MockBean
    protected RedisUtils redisUtils;
    protected JwtTokenUtils jwtTokenUtils;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = Member.builder()
                .id(1L)
                .name("김길동")
                .email("gddong@gmail.com")
                .roles(List.of("USER"))
                .build();

        lesson = Lesson.builder()
                .id(1L)
                .name("홍자루")
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

        bookmark = Bookmark.builder()
                .id(1L)
                .lessonId(1L)
                .memberId(1L)
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
