package com.codueon.boostUp.domain.bookmark.controller;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.bookmark.repository.BookmarkRepository;
import com.codueon.boostUp.domain.bookmark.service.BookmarkService;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.member.entity.Member;
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

    @BeforeEach
    void setUp() throws Exception {
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

        Language language = Language.builder()
                .id(1L)
                .languages("Java")
                .build();

        LessonLanguage lessonLanguage = LessonLanguage.builder()
                .id(1L)
                .languages(language)
                .build();

        Address address = Address.builder()
                .id(1L)
                .address("강남구")
                .build();

        LessonAddress lessonAddress = LessonAddress.builder()
                .id(1L)
                .address(address)
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
    }
}
