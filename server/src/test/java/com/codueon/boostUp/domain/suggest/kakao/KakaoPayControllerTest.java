package com.codueon.boostUp.domain.suggest.kakao;

import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.suggest.contoller.KakaoPayController;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.service.KakaoPayService;
import com.codueon.boostUp.domain.suggest.service.PaymentService;
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
@WebMvcTest(controllers = {KakaoPayController.class})
public class KakaoPayControllerTest {
    @Autowired
    protected Gson gson;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected GlobalAdvice globalAdvice;

    @MockBean
    protected KakaoPayService kakaoPayService;

    @MockBean
    protected PaymentService paymentService;

    protected DataForTest data = new DataForTest();

    protected Member member;
    protected Suggest suggest;
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

    protected List<FieldDescriptor> messageResponse() {
        return List.of(
                fieldWithPath("data").type(JsonFieldType.STRING).description("데이터"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지")
        );
    }
}
