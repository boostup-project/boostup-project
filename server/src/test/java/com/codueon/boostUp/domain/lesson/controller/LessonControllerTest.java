package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.lesson.dto.Get.GetMainPageLesson;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

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
                        .name(member.getName())
                        .lesson(lessons)
                        .bookmark(false)
                        .build())
                .collect(Collectors.toList());

        mainPageResponse2 = lessonList.stream()
                .map(lessons -> GetMainPageLesson.builder()
                        .name(member.getName())
                        .lesson(lessons)
                        .bookmark(true)
                        .build())
                .collect(Collectors.toList());

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getName(), null, member.getId(), false, accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected List<FieldDescriptor> getLessonPageResponse() {
        return List.of(
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                fieldWithPath("data[0].lessonId").type(JsonFieldType.NUMBER).description("과외 식별자"),
                fieldWithPath("data[0].name").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("data[0].title").type(JsonFieldType.STRING).description("과외 타이틀"),
                fieldWithPath("data[0].cost").type(JsonFieldType.NUMBER).description("과외 가격"),
                fieldWithPath("data[0].profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                fieldWithPath("data[0].name").type(JsonFieldType.STRING).description("강사 이름"),
                fieldWithPath("data[0].company").type(JsonFieldType.STRING).description("강사 대표회사"),
                fieldWithPath("data[0].career").type(JsonFieldType.NUMBER).description("강사 경력"),
                fieldWithPath("data[0].languages").type(JsonFieldType.ARRAY).description("과외 가능 언어"),
                fieldWithPath("data[0].address").type(JsonFieldType.ARRAY).description("과외 가능 지역"),
                fieldWithPath("data[0].bookmark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),

                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
        );
    }
}
