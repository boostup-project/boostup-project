package com.codueon.boostUp.domain.review.controller;

import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.reveiw.controller.ReviewController;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.reveiw.service.ReviewService;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.global.webhook.SendErrorToDiscord;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

@WithMockUser
@AutoConfigureWebMvc
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
    protected SuggestDbService suggestDbService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected Member member;
    protected Review review;
    protected Lesson lesson;
    protected Suggest suggest;

    @BeforeEach
    void setUp() {
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
    }

}
