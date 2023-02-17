package com.codueon.boostUp.domain.suggest.payment;

import com.codueon.boostUp.domain.suggest.dto.GetPaymentInfo;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentReceipt;
import com.codueon.boostUp.domain.suggest.dto.GetRefundPayment;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentStatusCheck;
import com.codueon.boostUp.domain.suggest.kakao.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.toss.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.CANCELED_PAY_MESSAGE;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.codueon.boostUp.domain.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.codueon.boostUp.global.security.utils.AuthConstants.*;
import static com.codueon.boostUp.global.security.utils.AuthConstants.REFRESH_TOKEN;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentTest extends PaymentControllerTest {

    @Test
    @DisplayName("GET 신청 프로세스 3 결제 페이지 조회")
    void getPaymentInfo() throws Exception {
        Integer quantity = 5;
        Integer totalCost = 250000;
        String name = "김선생";

        lesson.addProfileImage(profileImage);

        GetPaymentInfo getPaymentInfo =
                new GetPaymentInfo(lesson, name, totalCost, quantity);

        given(suggestDbService.getPaymentInfoOnMyPage(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/payment/info", suggest.getLessonId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(getPaymentInfo.getTotalCost()))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("신청3-결제페이지",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getPaymentInfoResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 9 결제 여부 조회")
    void paymentStatusCheck() throws Exception {
        Boolean paymentCheck = true;
        GetPaymentStatusCheck getPaymentStatusCheck = new GetPaymentStatusCheck(paymentCheck);

        given(paymentService.getPaymentStatusCheck(suggest.getId(), suggest.getMemberId()))
                .willReturn(getPaymentStatusCheck.getPaymentCheck());

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/payment/check", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentCheck").value(true))
                .andDo(document("신청9-결제여부조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("paymentCheck").type(JsonFieldType.BOOLEAN).description("결제 여부")
                        )
                ));

    }

    @Test
    @DisplayName("GET 결제 영수증 조회")
    void getPaymentReceipt() throws Exception {
        Integer totalCost = 50000;
        Integer quantity = 5;
        String paymentMethod = "카카오페이";
        String name = "김선생";

        GetPaymentReceipt getPaymentInfo =
                new GetPaymentReceipt(lesson, name, totalCost, quantity, paymentMethod);

        given(suggestDbService.getPaymentReceiptOnMyPage(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(getPaymentInfo);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/receipt", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.paymentMethod").value(paymentMethod))
                .andExpect(jsonPath("$.title").value(getPaymentInfo.getTitle()))
                .andExpect(jsonPath("$.name").value(getPaymentInfo.getName()))
                .andExpect(jsonPath("$.company").value(getPaymentInfo.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(getPaymentInfo.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(getPaymentInfo.getCost()))
                .andExpect(jsonPath("$.totalCost").value(totalCost))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("결제영수증조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getPaymentReceiptResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 10-1 Kakao 환불")
    void refundPaymentKakao() throws Exception {
        Message message = Message.builder()
                .data(getKakaoPayCancelInfo())
                .message(CANCELED_PAY_MESSAGE)
                .build();

        given(paymentService.refundPaymentKakaoOrToss(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/refund", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청10.1-카카오결제환불",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("GET 신청 프로세스 10-2 Toss 환불")
    void refundPaymentToss() throws Exception {
        Integer count = 2;
        Integer amount = 5000;

        RequestForTossPayCancelInfo body = RequestForTossPayCancelInfo.builder()
                .cancelReason("고객이 취소를 요청함")
                .cancelAmount(count * amount)
                .build();

        List<Cancels> cancels = new ArrayList<>();
        Checkout checkout = Checkout.builder().build();
        TossCard card = TossCard.builder().build();
        Receipt receipt = Receipt.builder().build();
        MobilePhone mobilePhone = MobilePhone.builder().build();
        Transfer transfer = Transfer.builder().build();

        TossPayCancelInfo cancelInfo = TossPayCancelInfo.builder()
                .cancels(cancels)
                .totalAmount(count * amount)
                .paymentKey("paymentKey")
                .lastTransactionKey("lastTransactionKey")
                .method("method")
                .orderId("orderId")
                .orderName("orderName")
                .checkout(checkout)
                .mId("mId")
                .requestedAt("requestedAt")
                .approvedAt("approvedAt")
                .card(card)
                .receipt(receipt)
                .mobilePhone(mobilePhone)
                .transfer(transfer)
                .build();

        Message message = Message.builder()
                .data(cancelInfo)
                .message(CANCELED_PAY_MESSAGE)
                .build();

        given(paymentService.refundPaymentKakaoOrToss(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/refund", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("신청10.2-토스결제환불",
                        getRequestPreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("GET 환불 영수증 조회")
    void getRefundPaymentInfo() throws Exception {
        String name = "김선생";

        GetRefundPayment response = GetRefundPayment.builder()
                .name(name)
                .lesson(lesson)
                .totalCost(suggest.getTotalCost())
                .quantity(paymentInfo.getQuantity())
                .paymentMethod(suggest.getPaymentMethod())
                .quantityCount(paymentInfo.getQuantityCount())
                .build();

        given(paymentService.getRefundPaymentInfo(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get("/suggest/{suggest-id}/refund/info", suggest.getId())
                                .header(AUTHORIZATION, BEARER + accessToken)
                                .header(REFRESH_TOKEN, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.quantityCount").value(response.getQuantityCount()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.company").value(response.getCompany()))
                .andExpect(jsonPath("$.profileImage").value(response.getProfileImage()))
                .andExpect(jsonPath("$.cost").value(response.getCost()))
                .andExpect(jsonPath("$.totalCost").value(response.getTotalCost()))
                .andExpect(jsonPath("$.languages[0]").value("Javascript"))
                .andExpect(jsonPath("$.languages[1]").value("Python"))
                .andExpect(jsonPath("$.languages[2]").value("Go"))
                .andExpect(jsonPath("$.address[0]").value("강남구"))
                .andExpect(jsonPath("$.address[1]").value("강동구"))
                .andExpect(jsonPath("$.address[2]").value("강북구"))
                .andDo(document("환불영수증조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("suggest-id").description("신청 식별자")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH_TOKEN).description("리프레시 토큰")
                        ),
                        responseFields(
                                getRefundPaymentInfoResponse()
                        )
                ));

    }

    private KakaoPayCancelInfo getKakaoPayCancelInfo() {
        Integer count = 2;
        Integer cost = 5000;

        RequestForKakaoPayCancelInfo param = RequestForKakaoPayCancelInfo.builder()
                .tid("tid")
                .cid("cid")
                .cancel_tax_free_amount(1000)
                .cancel_amount(count * cost)
                .build();

        Amount amount = Amount.builder()
                .total(suggest.getTotalCost())
                .taxFree(param.getCancel_tax_free_amount())
                .vat(1000)
                .discount(0)
                .point(0)
                .build();

        ApprovedCancelAmount approvedCancelAmount = ApprovedCancelAmount.builder()
                .total(param.getCancel_amount())
                .vat(1000)
                .build();

        CanceledAmount canceledAmount = CanceledAmount.builder()
                .total(param.getCancel_amount())
                .vat(1000)
                .build();

        CancelAvailableAmount cancelAvailableAmount = CancelAvailableAmount.builder()
                .total(param.getCancel_amount())
                .build();

        Date date = new Date(2023, 01, 19, 05, 25);

        return KakaoPayCancelInfo.builder()
                .aid("aid")
                .tid(param.getTid())
                .cid(param.getCid())
                .status("status")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("paymentMethodType")
                .amount(amount)
                .approvedCancelAmount(approvedCancelAmount)
                .canceledAmount(canceledAmount)
                .cancelAvailableAmount(cancelAvailableAmount)
                .itemName("itemName")
                .itemCode("itemCode")
                .quantity(paymentInfo.getQuantity())
                .createdAt(date)
                .approvedAt(date)
                .canceledAt(date)
                .orderStatus("orderStatus")
                .build();
    }
}
