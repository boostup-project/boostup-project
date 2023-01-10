package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.suggest.dto.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.CANCELED_PAY_MESSAGE;
import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.FAILED_PAY_MESSAGE;

@RestController
@RequiredArgsConstructor
public class SuggestController {

    private final SuggestDbService suggestDbService;
    private final SuggestService suggestService;

    /**
     * 신청 프로세스 1 신청 생성 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param post 신청 생성 DTO
     * @return ResponseEntity
     * @author LeeGoh
     */
    @PostMapping("/suggest/lesson/{lesson-id}")
    public ResponseEntity<?> createSuggest(@PathVariable("lesson-id") Long lessonId,
                                      @RequestBody @Valid PostSuggest post) {

        Long memberId = 1L;
        suggestService.createSuggest(post, lessonId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 신청 프로세스 2-1 신청 수락 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @param post quantity 과외 횟수 DTO
     * @return ResponseEntity
     * @author LeeGoh
     */
    @PostMapping("/suggest/{suggest-id}/accept")
    public ResponseEntity acceptSuggest(@PathVariable("suggest-id") Long suggestId,
                                        @RequestBody PostPaymentUrl post) {

        Long memberId = 1L;
        suggestService.acceptSuggest(suggestId, memberId, post.getQuantity());
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 2-2 신청 거절 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @param postReason 거절 사유 DTO
     * @return ResponseEntity
     * @author LeeGoh
     */
    @PostMapping("/suggest/{suggest-id}/decline")
    public ResponseEntity declineSuggest(@PathVariable("suggest-id") Long suggestId,
                                         @RequestBody PostReason postReason) {

        Long memberId = 1L;
        suggestService.declineSuggest(suggestId, memberId, postReason);
        return ResponseEntity.noContent().build();
    }

    /**
     * 신청 프로세스 3 결제 페이지 요청 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/suggest-info")
    public ResponseEntity getSuggestInfo(@PathVariable("suggest-id") Long suggestId) {

        Long memberId = 1L;
        return ResponseEntity.ok(suggestService.getSuggestInfo(suggestId, memberId));
    }

    /**
     * 신청 프로세스 4-1 결제 URL 요청 컨트롤러 메서드 Kakao
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/kakao/payment")
    public ResponseEntity<Message<?>> orderKakaoPayment(@PathVariable("suggest-id") Long suggestId,
                                       HttpServletRequest request) {

        Long memberId = 1L;
        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        Message<?> message = suggestService.getKaKapPayUrl(suggestId, memberId, requestUrl);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

    /**
     * 신청 프로세스 4-2 결제 URL 요청 컨트롤러 메서드 Toss
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/toss/payment/{payment-id}")
    public ResponseEntity<Message<?>> orderTossPayment(@PathVariable("suggest-id") Long suggestId,
                                                       @PathVariable("payment-id") int paymentId,
                                                       HttpServletRequest request) {

        Long memberId = 1L;
        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        Message<?> message = suggestService.getTossPayUrl(suggestId, requestUrl, paymentId);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

    /**
     * 신청 프로세스 5-1 결제 성공 컨트롤러 메서드 Kakao
     * @param suggestId 신청 식별자
     * @param pgToken Payment Gateway Token
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/kakao/success")
    public ResponseEntity<Message<?>> successKakaoPayment(@PathVariable("suggest-id") Long suggestId,
                                                          @RequestParam("pg_token") String pgToken) {

        Message<?> message = suggestService.getSuccessKakaoPaymentInfo(suggestId, pgToken);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 5-2 결제 성공 컨트롤러 메서드 Toss
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/toss/success")
    public ResponseEntity<Message<?>> successTossPayment(@PathVariable("suggest-id") Long suggestId) {

        Message<?> message = suggestService.getSuccessTossPaymentInfo(suggestId);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 6 결제 취소 컨트롤러 메서드 Kakao
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
     * @param suggestId 신청 식별자
     * @return String
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/kakao/failure")
    public ResponseEntity<String> failedKakaoPayment(@PathVariable("suggest-id") Long suggestId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }

    /**
     * 신청 프로세스 7-2 결제 실패 컨트롤러 메서드 Toss
     * @param suggestId 신청 식별자
     * @return String
     * @author LeeGoh
     */
    @GetMapping("/api/suggest/{suggest-id}/toss/failure")
    public ResponseEntity<String> failedTossPayment(@PathVariable("suggest-id") Long suggestId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }

    /**
     * 신청 프로세스 8 과외 종료 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/done")
    public ResponseEntity endOfLesson(@PathVariable("suggest-id") Long suggestId) {

        suggestService.setSuggestStatusAndEndTime(suggestId);
        return ResponseEntity.ok().build();
    }

    /**
     * 마이페이지 선생님용 신청내역 조회 컨트롤러 메서드
     * @param lessonId 과외 식별자
     * @param tabId 탭 번호
     * @param pageable 페이지 정보
     * @return MultiResponseDto
     * @author LeeGoh
     */
    @GetMapping("/suggest/lesson/{lesson-id}/tutor/tab/{tab-id}")
    public ResponseEntity<MultiResponseDto<?>> getTutorSuggest(@PathVariable("lesson-id") Long lessonId,
                                                               @PathVariable("tab-id") int tabId,
                                                               Pageable pageable) {

        Long memberId = 1L;
        Page<GetTutorSuggest> suggestions = suggestDbService.getTutorSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    /**
     * 마이페이지 학생용 신청내역 조회 컨트롤러 메서드
     * @param pageable 페이지 정보
     * @return MultiResponseDto
     * @author LeeGoh
     */
    @GetMapping("/suggest/student")
    public ResponseEntity<MultiResponseDto<?>> getStudentSuggest(Pageable pageable) {

        Long memberId = 1L;
        Page<GetStudentSuggest> suggestions = suggestDbService.getStudentSuggestsOnMyPage(memberId, pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    /**
     * 신청 취소 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/suggest/{suggest-id}")
    public ResponseEntity cancelSuggest(@PathVariable("suggest-id") Long suggestId) {

        Long memberId = 1L;
        suggestService.cancelSuggest(suggestId, memberId);
        return ResponseEntity.noContent().build();
    }

}
