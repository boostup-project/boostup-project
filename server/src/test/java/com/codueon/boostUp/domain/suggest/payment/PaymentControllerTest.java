package com.codueon.boostUp.domain.suggest.payment;


import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.contoller.PaymentController;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.*;
import com.codueon.boostUp.domain.utils.DataForTest;
import com.codueon.boostUp.global.exception.GlobalAdvice;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

import static com.codueon.boostUp.domain.suggest.entity.SuggestStatus.DURING_LESSON;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {PaymentController.class})
public class PaymentControllerTest {
    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GlobalAdvice globalAdvice;

    @MockBean
    protected SuggestDbService suggestDbService;

    @MockBean
    protected PaymentService paymentService;

    protected DataForTest data = new DataForTest();

    protected Member member;
    protected Suggest suggest;
    protected Lesson lesson;
    protected ProfileImage profileImage;
    protected PaymentInfo paymentInfo;

    @MockBean
    protected RedisUtils redisUtils;
    protected JwtTokenUtils jwtTokenUtils;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils =
                new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE, objectMapper);

        member = data.getMember1();
        suggest = data.getSuggest1();
        suggest.setStartTime();
        suggest.setEndTime();
        suggest.setStatus(DURING_LESSON);
        lesson = data.getLesson1();
        profileImage = data.getProfileImage();
        paymentInfo = PaymentInfo.builder()
                .quantity(5)
                .build();
        paymentInfo.setQuantityCount(3);

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

    protected List<FieldDescriptor> getPaymentInfoResponse() {
        return List.of(
                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("과외 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("강사 이름"),
                fieldWithPath("company").type(JsonFieldType.STRING).description("강사 경력"),
                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("회당 가격"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER).description("총 가격"),

                fieldWithPath("languages").type(JsonFieldType.ARRAY).description("과외 가능 언어 리스트"),
                fieldWithPath("address").type(JsonFieldType.ARRAY).description("과외 가능 지역 리스트")
        );
    }

    protected List<FieldDescriptor> getPaymentReceiptResponse() {
        return List.of(
                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("과외 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("강사 이름"),
                fieldWithPath("company").type(JsonFieldType.STRING).description("강사 경력"),
                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("회당 가격"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("paymentMethod").type(JsonFieldType.STRING).description("결제 방식"),

                fieldWithPath("languages").type(JsonFieldType.ARRAY).description("과외 가능 언어 리스트"),
                fieldWithPath("address").type(JsonFieldType.ARRAY).description("과외 가능 지역 리스트")
        );
    }

    protected List<FieldDescriptor> getRefundPaymentInfoResponse() {
        return List.of(
                fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("과외 횟수"),
                fieldWithPath("quantityCount").type(JsonFieldType.NUMBER).description("출석 인정 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("과외 타이틀"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("강사 이름"),
                fieldWithPath("company").type(JsonFieldType.STRING).description("강사 경력"),
                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("섬네일 이미지"),
                fieldWithPath("cost").type(JsonFieldType.NUMBER).description("회당 가격"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER).description("총 가격"),
                fieldWithPath("cancelCost").type(JsonFieldType.NUMBER).description("취소 가격"),
                fieldWithPath("paymentMethod").type(JsonFieldType.STRING).description("결제 방법"),

                fieldWithPath("languages").type(JsonFieldType.ARRAY).description("과외 가능 언어 리스트"),
                fieldWithPath("address").type(JsonFieldType.ARRAY).description("과외 가능 지역 리스트")
        );
    }
}
