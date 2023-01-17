package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.exception.GlobalAdvice;
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
@WebMvcTest(controllers = {SuggestController.class})
class SuggestControllerTest {

    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GlobalAdvice globalAdvice;

    @MockBean
    protected SuggestService suggestService;

    @MockBean
    protected SuggestDbService suggestDbService;

    @MockBean
    protected SendErrorToDiscord sendErrorToDiscord;

    protected DataForTest data = new DataForTest();

    protected Member member;
    protected Suggest suggest;
    protected Lesson lesson;
    protected ProfileImage profileImage;
    protected PaymentInfo paymentInfo;

    @BeforeEach
    void setUp() throws Exception {
        member = data.getMember1();
        suggest = data.getSuggest1();
        lesson = data.getLesson1();
        profileImage = data.getProfileImage();
        paymentInfo = PaymentInfo.builder()
                .quantity(5)
                .build();
        paymentInfo.setQuantityCount(3);
    }
}