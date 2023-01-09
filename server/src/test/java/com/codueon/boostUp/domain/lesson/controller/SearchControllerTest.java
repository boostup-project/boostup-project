package com.codueon.boostUp.domain.lesson.controller;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.lesson.dto.GetMainPageLesson;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.lesson.service.SearchService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.utils.DataForTest;
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
import java.util.stream.Collectors;

@WithMockUser
@AutoConfigureWebMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {SearchController.class})
public class SearchControllerTest {

    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected SearchService searchService;

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

    @BeforeEach
    void setUp() {
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
    }
}
