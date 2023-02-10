package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.suggest.dto.*;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import com.codueon.boostUp.domain.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param lessonId       과외 식별자
     * @param post           신청 생성 DTO
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @PostMapping("/suggest/lesson/{lesson-id}")
    public ResponseEntity<?> createSuggest(@PathVariable("lesson-id") Long lessonId,
                                           @RequestBody @Valid PostSuggest post,
                                           Authentication authentication) {
        suggestService.createSuggest(post, lessonId, AuthVO.of(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 신청 프로세스 2-1 신청 수락 컨트롤러 메서드
     *
     * @param suggestId      신청 식별자
     * @param post           quantity 과외 횟수 DTO
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @PostMapping("/suggest/{suggest-id}/accept")
    public ResponseEntity<?> acceptSuggest(@PathVariable("suggest-id") Long suggestId,
                                        @RequestBody @Valid PostPaymentUrl post,
                                        Authentication authentication) {
        suggestService.acceptSuggest(suggestId, AuthVO.of(authentication), post.getQuantity());
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 2-2 신청 거절 컨트롤러 메서드
     *
     * @param suggestId      신청 식별자
     * @param postReason     거절 사유 DTO
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @PostMapping("/suggest/{suggest-id}/decline")
    public ResponseEntity<?> declineSuggest(@PathVariable("suggest-id") Long suggestId,
                                         @RequestBody @Valid PostReason postReason,
                                         Authentication authentication) {
        suggestService.declineSuggest(suggestId, AuthVO.of(authentication), postReason);
        return ResponseEntity.noContent().build();
    }

    /**
     * 신청 프로세스 3 결제 페이지 요청 컨트롤러 메서드
     *
     * @param suggestId      신청 식별자
     * @param authentication 인증 정보
     * @return GetPaymentInfo 결제 페이지 정보
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/payment/info")
    public ResponseEntity<GetPaymentInfo> getPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                         Authentication authentication) {
        return ResponseEntity.ok(suggestDbService.getPaymentInfoOnMyPage(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 신청 프로세스 4-1 결제 URL 요청 컨트롤러 메서드 Kakao
     *
     * @param suggestId 신청 식별자
     * @return Message 메시지
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/kakao/payment")
    public ResponseEntity<Message<?>> orderKakaoPayment(@PathVariable("suggest-id") Long suggestId) {

        Message<?> message = suggestService.getKaKapPayUrl(suggestId);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

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

        Message<?> message = suggestService.getTossPayUrl(suggestId, paymentId);
        if (message.getData() == null) suggestService.getFailedPayMessage();
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
        Message<?> message = suggestService.getSuccessKakaoPaymentInfo(suggestId, pgToken);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().build();
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
        Message<?> message = suggestService.getSuccessTossPaymentInfo(suggestId);
        if (message.getData() == null) suggestService.getFailedPayMessage();
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

    /**
     * 신청 프로세스 8 결제 여부 확인
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return WrapPaymentStatusCheck
     * @author LeeGoh
     */
    @GetMapping("suggest/{suggest-id}/payment/check")
    public ResponseEntity<WrapPaymentStatusCheck> paymentStatusCheck(@PathVariable("suggest-id") Long suggestId,
                                             Authentication authentication) {
        Boolean paymentCheck = suggestService.getPaymentStatusCheck(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(new WrapPaymentStatusCheck(paymentCheck));
    }

    /**
     * 신청 프로세스 9 과외 종료 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/done")
    public ResponseEntity<?> endOfLesson(@PathVariable("suggest-id") Long suggestId,
                                      Authentication authentication) {
        suggestService.setSuggestStatusAndEndTime(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().build();
    }

    /**
     * 신청 프로세스 10 환불 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return Message
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable("suggest-id") Long suggestId,
                                                 Authentication authentication) {
        Message message = suggestService.refundPaymentKakaoOrToss(suggestId, AuthVO.ofMemberId(authentication));
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
    @GetMapping("/suggest/{suggest-id}/refund/info")
    public ResponseEntity<GetRefundPayment> getRefundPaymentInfo(@PathVariable("suggest-id") Long suggestId,
                                               Authentication authentication) {
        return ResponseEntity.ok().body(suggestService.getRefundPaymentInfo(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 출석부 1 출석부 조회 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance")
    public ResponseEntity<GetLessonAttendance> getLessonAttendance(@PathVariable("suggest-id") Long suggestId,
                                              Authentication authentication) {
        return ResponseEntity.ok(suggestService.getLessonAttendance(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 출석부 2 출석 인정(출석 횟수 차감) 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance/check")
    public ResponseEntity<WrapQuantityCount> lessonAttendanceCheck(@PathVariable("suggest-id") Long suggestId,
                                                Authentication authentication) {
        Integer quantityCount = suggestService.teacherChecksAttendance(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(new WrapQuantityCount(quantityCount));
    }

    /**
     * 출석부 3 출석 인정 취소(출석 횟수 증가) 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/attendance/cancel")
    public ResponseEntity<WrapQuantityCount> lessonAttendanceCancel(@PathVariable("suggest-id") Long suggestId,
                                                 Authentication authentication) {
        Integer quantityCount = suggestService.teacherCancelAttendance(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(new WrapQuantityCount(quantityCount));
    }

    /**
     * 결제 영수증 조회 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/suggest/{suggest-id}/receipt")
    public ResponseEntity<GetPaymentReceipt> getPaymentReceipt(@PathVariable("suggest-id") Long suggestId,
                                            Authentication authentication) {
        return ResponseEntity.ok(suggestDbService.getPaymentReceiptOnMyPage(suggestId, AuthVO.ofMemberId(authentication)));
    }

    /**
     * 마이페이지 선생님용 신청내역 조회 컨트롤러 메서드
     *
     * @param lessonId 과외 식별자
     * @param tabId    탭 번호
     * @param authentication 인증 정보
     * @param pageable 페이지 정보
     * @return MultiResponseDto
     * @author LeeGoh
     */
    @GetMapping("/suggest/lesson/{lesson-id}/tutor/tab/{tab-id}")
    public ResponseEntity<MultiResponseDto<?>> getTutorSuggest(@PathVariable("lesson-id") Long lessonId,
                                                               @PathVariable("tab-id") int tabId,
                                                               Authentication authentication,
                                                               Pageable pageable) {
        Page<GetTutorSuggest> suggestions = suggestDbService.getTutorSuggestsOnMyPage(lessonId, AuthVO.ofMemberId(authentication), tabId, pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    /**
     * 마이페이지 학생용 신청내역 조회 컨트롤러 메서드
     *
     * @param pageable 페이지 정보
     * @param authentication 인증 정보
     * @return MultiResponseDto
     * @author LeeGoh
     */
    @GetMapping("/suggest/student")
    public ResponseEntity<MultiResponseDto<?>> getStudentSuggest(Pageable pageable,
                                                                 Authentication authentication) {
        Page<GetStudentSuggest> suggestions = suggestDbService.getStudentSuggestsOnMyPage(AuthVO.ofMemberId(authentication), pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    /**
     * 신청 취소 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/suggest/{suggest-id}")
    public ResponseEntity<?> cancelSuggest(@PathVariable("suggest-id") Long suggestId,
                                        Authentication authentication) {
        suggestService.cancelSuggest(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.noContent().build();
    }

    /**
     * 강사 종료 과외 삭제
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/suggest/{suggest-id}/tutor")
    public ResponseEntity<?> deleteTutorEndOfSuggest(@PathVariable("suggest-id") Long suggestId,
                                                  Authentication authentication) {
        suggestService.deleteTutorEndOfSuggest(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.noContent().build();
    }

    /**
     * 학생 종료 과외 삭제
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @DeleteMapping("/suggest/{suggest-id}/student")
    public ResponseEntity<?> deleteStudentEndOfSuggest(@PathVariable("suggest-id") Long suggestId,
                                                    Authentication authentication) {
        suggestService.deleteStudentEndOfSuggest(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.noContent().build();
    }
}
