package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.CANCELED_PAY_MESSAGE;
import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.FAILED_PAY_MESSAGE;

@RestController
@RequiredArgsConstructor
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

    /**
     * 신청 프로세스 4-1 결제 URL 요청 컨트롤러 메서드 Kakao
     *
     * @param suggestId 신청 식별자
     * @return Message 메시지
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/kakao/payment")
    public ResponseEntity<Message<?>> orderKakaoPayment(@PathVariable("suggest-id") Long suggestId) {

        Message<?> message = kakaoPayService.getKaKaoPayUrl(suggestId);
        if (message.getData() == null) kakaoPayService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

    /**
     * 신청 프로세스 5-1 결제 성공 컨트롤러 메서드 Kakao
     *
     * @param suggestId 신청 식별자
     * @param pgToken   Payment Gateway Token
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/kakao/success")
    public ResponseEntity<Message<?>> successKakaoPayment(@PathVariable("suggest-id") Long suggestId,
                                                          @RequestParam("pg_token") String pgToken) {
        Message<?> message = kakaoPayService.getSuccessKakaoPaymentInfo(suggestId, pgToken);
        if (message.getData() == null) kakaoPayService.getFailedPayMessage();
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 6 결제 취소 컨트롤러 메서드 Kakao
     *
     * @param suggestId 신청 식별자
     * @return String
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/kakao/cancellation")
    public ResponseEntity<String> cancelKakaoPayment(@PathVariable("suggest-id") Long suggestId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(CANCELED_PAY_MESSAGE);
    }

    /**
     * 신청 프로세스 7-1 결제 실패 컨트롤러 메서드 Kakao
     *
     * @param suggestId 신청 식별자
     * @return String
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/kakao/failure")
    public ResponseEntity<String> failedKakaoPayment(@PathVariable("suggest-id") Long suggestId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }
}
