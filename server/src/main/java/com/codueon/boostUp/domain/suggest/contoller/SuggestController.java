package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.suggest.dto.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                           @RequestBody @Valid PostSuggest post,
                                           Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        suggestService.createSuggest(post, lessonId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("suggest/ticket/{ticket-id}")
    public ResponseEntity createAdvertisement(@PathVariable("ticket-id") Long ticketId,
                                              Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        suggestService.createPurchase(ticketId, memberId);
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
                                        @RequestBody PostPaymentUrl post,
                                        Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
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
                                         @RequestBody PostReason postReason,
                                         Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        suggestService.declineSuggest(suggestId, memberId, postReason);
        return ResponseEntity.noContent().build();
    }

    /**
     * 신청 프로세스 3 결제 페이지 요청 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/payment/info")
    public ResponseEntity getPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                         Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok(suggestDbService.getPaymentInfoOnMyPage(suggestId, memberId));
    }

    /**
     * 신청 프로세스 4-1 결제 URL 요청 컨트롤러 메서드 Kakao
     * @param suggestId 신청 식별자
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/kakao/payment")
    public ResponseEntity<Message<?>> orderKakaoPayment(@PathVariable("suggest-id") Long suggestId,
                                                        HttpServletRequest request,
                                                        Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        Message<?> message = suggestService.getKaKapPayUrl(suggestId, requestUrl);
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
                                                       HttpServletRequest request,
                                                       Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);

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
     * 신청 프로세스 8 결제 여부 확인
     * @param suggestId 신청 식별자
     * @return WrapPaymentStatusCheck
     * @author LeeGoh
     */
    @GetMapping("suggest/{suggest-id}/payment/check")
    public ResponseEntity paymentStatusCheck(@PathVariable("suggest-id") Long suggestId,
                                             Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        Boolean paymentCheck = suggestService.getPaymentStatusCheck(suggestId, memberId);
        return ResponseEntity.ok().body(new WrapPaymentStatusCheck(paymentCheck));
    }

    /**
     * 신청 프로세스 9 과외 종료 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/done")
    public ResponseEntity endOfLesson(@PathVariable("suggest-id") Long suggestId,
                                      Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        suggestService.setSuggestStatusAndEndTime(suggestId, memberId);
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 10 환불 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/refund")
    public ResponseEntity<Message> refundPayment(@PathVariable("suggest-id") Long suggestId,
                                                 Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        Message message = suggestService.refundPaymentKakaoOrToss(suggestId, memberId);
        return ResponseEntity.ok().body(message);
    }

    /**
     * 환불 조회 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/refund/info")
    public ResponseEntity getRefundPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                               Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok().body(suggestService.getRefundPaymentInfo(suggestId, memberId));
    }

    /**
     * 출석부 1 출석부 조회 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance")
    public ResponseEntity getLessonAttendance(@PathVariable("suggest-id") Long suggestId,
                                              Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok(suggestService.getLessonAttendance(suggestId, memberId));
    }

    /**
     * 출석부 2 출석 인정(출석 횟수 차감) 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance/check")
    public ResponseEntity lessonAttendanceCheck(@PathVariable("suggest-id") Long suggestId,
                                                         Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        Integer quantityCount = suggestService.teacherChecksAttendance(suggestId, memberId);
        return ResponseEntity.ok().body(new WrapQuantityCount(quantityCount));
    }

    /**
     * 출석부 3 출석 인정 취소(출석 횟수 증가) 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance/cancel")
    public ResponseEntity lessonAttendanceCancel(@PathVariable("suggest-id") Long suggestId,
                                                          Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        Integer quantityCount = suggestService.teacherCancelAttendance(suggestId, memberId);
        return ResponseEntity.ok().body(new WrapQuantityCount(quantityCount));
    }

    /**
     * 결제 영수증 조회 컨트롤러 메서드
     * @param suggestId 신청 식별자
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/receipt")
    public ResponseEntity getPaymentReceipt(@PathVariable("suggest-id") Long suggestId,
                                            Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        return ResponseEntity.ok(suggestDbService.getPaymentReceiptOnMyPage(suggestId, memberId));
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
                                                               Authentication authentication,
                                                               Pageable pageable) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
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
    public ResponseEntity<MultiResponseDto<?>> getStudentSuggest(Pageable pageable,
                                                                 Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
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
    public ResponseEntity cancelSuggest(@PathVariable("suggest-id") Long suggestId,
                                        Authentication authentication) {

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Long memberId = getMemberIdIfExistToken(token);
        suggestService.cancelSuggest(suggestId, memberId);
        return ResponseEntity.noContent().build();
    }

    private Long getMemberIdIfExistToken(JwtAuthenticationToken token) {
        if (token == null) return null;
        else return token.getId();
    }

}
