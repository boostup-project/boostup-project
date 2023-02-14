package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.GetPaymentInfo;
import com.codueon.boostUp.domain.suggest.dto.GetPaymentReceipt;
import com.codueon.boostUp.domain.suggest.dto.GetRefundPayment;
import com.codueon.boostUp.domain.suggest.dto.WrapPaymentStatusCheck;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.PaymentService;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggest")
public class PaymentController {
    private final SuggestDbService suggestDbService;
    private final PaymentService paymentService;

    /**
     * 신청 프로세스 3 결제 페이지 요청 컨트롤러 메서드
     *
     * @param suggestId      신청 식별자
     * @param authentication 인증 정보
     * @return GetPaymentInfo 결제 페이지 정보
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/payment/info")
    public ResponseEntity<GetPaymentInfo> getPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                                         Authentication authentication) {
        return ResponseEntity.ok(suggestDbService.getPaymentInfoOnMyPage(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 신청 프로세스 8 결제 여부 확인
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return WrapPaymentStatusCheck
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/payment/check")
    public ResponseEntity<WrapPaymentStatusCheck> paymentStatusCheck(@PathVariable("suggest-id") Long suggestId,
                                                                     Authentication authentication) {
        Boolean paymentCheck = paymentService.getPaymentStatusCheck(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(new WrapPaymentStatusCheck(paymentCheck));
    }

    /**
     * 결제 영수증 조회 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/receipt")
    public ResponseEntity<GetPaymentReceipt> getPaymentReceipt(@PathVariable("suggest-id") Long suggestId,
                                                               Authentication authentication) {
        return ResponseEntity.ok(suggestDbService.getPaymentReceiptOnMyPage(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 신청 프로세스 10 환불 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable("suggest-id") Long suggestId,
                                           Authentication authentication) {
        Message message = paymentService.refundPaymentKakaoOrToss(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(message);
    }

    /**
     * 환불 조회 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return GetRefundPayment
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/refund/info")
    public ResponseEntity<GetRefundPayment> getRefundPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                                                 Authentication authentication) {
        return ResponseEntity.ok().body(paymentService.getRefundPaymentInfo(suggestId, AuthVO.ofMemberId(authentication)));
    }
}
