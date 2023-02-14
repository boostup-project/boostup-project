package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.dto.MultiResponseDto;
import com.codueon.boostUp.domain.suggest.dto.*;
import com.codueon.boostUp.domain.suggest.service.SuggestDbService;
import com.codueon.boostUp.domain.suggest.service.SuggestService;
import com.codueon.boostUp.domain.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggest")
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
    @PostMapping("/lesson/{lesson-id}")
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
    @PostMapping("/{suggest-id}/accept")
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
    @PostMapping("/{suggest-id}/decline")
    public ResponseEntity<?> declineSuggest(@PathVariable("suggest-id") Long suggestId,
                                            @RequestBody @Valid PostReason postReason,
                                            Authentication authentication) {
        suggestService.declineSuggest(suggestId, AuthVO.of(authentication), postReason);
        return ResponseEntity.noContent().build();
    }

    /**
     * 신청 프로세스 9 과외 종료 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @return ResponseEntity
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/done")
    public ResponseEntity<?> endOfLesson(@PathVariable("suggest-id") Long suggestId,
                                         Authentication authentication) {
        suggestService.setSuggestStatusAndEndTime(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().build();
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
    @GetMapping("/lesson/{lesson-id}/tutor/tab/{tab-id}")
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
    @GetMapping("/student")
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
    @DeleteMapping("/{suggest-id}")
    public ResponseEntity<?> cancelSuggest(@PathVariable("suggest-id") Long suggestId,
                                        Authentication authentication) {
        suggestService.cancelSuggest(suggestId, AuthVO.of(authentication));
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
    @DeleteMapping("/{suggest-id}/tutor")
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
    @DeleteMapping("/{suggest-id}/student")
    public ResponseEntity<?> deleteStudentEndOfSuggest(@PathVariable("suggest-id") Long suggestId,
                                                       Authentication authentication) {
        suggestService.deleteStudentEndOfSuggest(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.noContent().build();
    }

    /**
     * 출석부 1 출석부 조회 컨트롤러 메서드
     *
     * @param suggestId 신청 식별자
     * @param authentication 인증 정보
     * @author LeeGoh
     */
    @GetMapping("/{suggest-id}/attendance")
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
    @GetMapping("/{suggest-id}/attendance/check")
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
    @GetMapping("/{suggest-id}/attendance/cancel")
    public ResponseEntity<WrapQuantityCount> lessonAttendanceCancel(@PathVariable("suggest-id") Long suggestId,
                                                                    Authentication authentication) {
        Integer quantityCount = suggestService.teacherCancelAttendance(suggestId, AuthVO.ofMemberId(authentication));
        return ResponseEntity.ok().body(new WrapQuantityCount(quantityCount));
    }
}
