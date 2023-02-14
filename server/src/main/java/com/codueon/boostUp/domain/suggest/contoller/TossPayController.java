package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.TossPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.FAILED_PAY_MESSAGE;

@RestController
@RequiredArgsConstructor
public class TossPayController {
    private final TossPayService tossPayService;

    /**
     * 신청 프로세스 4-2 결제 URL 요청 컨트롤러 메서드 Toss
     *
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/toss/payment/{payment-id}")
    public ResponseEntity<Message<?>> orderTossPayment(@PathVariable("suggest-id") Long suggestId,
                                                       @PathVariable("payment-id") int paymentId) {
        Message<?> message = tossPayService.getTossPayUrl(suggestId, paymentId);
        if (message.getData() == null) tossPayService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

    /**
     * 신청 프로세스 5-2 결제 성공 컨트롤러 메서드 Toss
     *
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/toss/success")
    public ResponseEntity<Message<?>> successTossPayment(@PathVariable("suggest-id") Long suggestId) {
        Message<?> message = tossPayService.getSuccessTossPaymentInfo(suggestId);
        if (message.getData() == null) tossPayService.getFailedPayMessage();
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 7-2 결제 실패 컨트롤러 메서드 Toss
     *
     * @param suggestId 신청 식별자
     * @return String
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/toss/failure")
    public ResponseEntity<String> failedTossPayment(@PathVariable("suggest-id") Long suggestId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }
}
