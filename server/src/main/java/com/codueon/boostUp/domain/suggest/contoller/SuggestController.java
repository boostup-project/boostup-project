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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SuggestController {

    private final SuggestDbService suggestDbService;
    private final SuggestService suggestService;

    @PostMapping("/lesson/{lesson-id}/suggest")
    public ResponseEntity<?> createSuggest(@PathVariable("lesson-id") Long lessonId,
                                      @RequestBody @Valid PostSuggest post) {

        Long memberId = 1L;
        suggestService.createSuggest(post, lessonId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/lesson/{lesson-id}/suggest/{suggest-id}/accept")
    public ResponseEntity acceptSuggest(@PathVariable("lesson-id") Long lessonId,
                                        @PathVariable("suggest-id") Long suggestId,
                                        @RequestBody Integer quantity) {

        Long memberId = 1L;
        suggestService.acceptSuggest(lessonId, suggestId, memberId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lesson/{lesson-id}/suggest/{suggest-id}/decline")
    public ResponseEntity declineSuggest(@PathVariable("lesson-id") Long lessonId,
                                         @PathVariable("suggest-id") Long suggestId,
                                         @RequestBody PostReason postReason) {


        Long memberId = 1L;
        suggestService.declineSuggest(lessonId, suggestId, memberId, postReason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lesson/{lesson-id}/suggest/{suggest-id}/suggest-info")
    public ResponseEntity getSuggestInfo(@PathVariable("suggest-id") Long suggestId,
                                         @PathVariable("lesson-id") Long lessonId) {

        Long memberId = 1L;
        return ResponseEntity.ok(suggestService.getSuggestInfo(suggestId, lessonId, memberId));
    }

    @GetMapping("/lesson/suggest/{suggest-id}/payment")
    public ResponseEntity<Message<?>> orderPayment(@PathVariable("suggest-id") Long suggestId,
                                       HttpServletRequest request) {

        Long memberId = 1L;
        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        Message<?> message = suggestService.getKaKapPayUrl(suggestId, memberId, requestUrl);
        if (message.getData() == null) suggestService.getFailedPayMessage();
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/api/suggest/{suggest-id}/completed")
    public ResponseEntity successPayment(@PathVariable("suggest-id") Long suggestId,
                                         @RequestParam("pg_token") String pgToken) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/suggest/{suggest-id}/cancel")
    public ResponseEntity cancelPayment(@PathVariable("suggest-id") Long suggestId) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/suggest/{suggest-id}/fail")
    public ResponseEntity failedPayment(@PathVariable("suggest-id") Long suggestId) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("lesson/{lesson-id}/suggest/tutor/tab/{tab-id}")
    public ResponseEntity<MultiResponseDto<?>> getTutorSuggest(@PathVariable("lesson-id") Long lessonId,
                                                                 @PathVariable("tab-id") int tabId,
                                                                 Pageable pageable) {

        Long memberId = 1L;
        Page<GetTutorSuggest> suggestions = suggestDbService.getTutorSuggestsOnMyPage(lessonId, memberId, tabId, pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    @GetMapping("/suggest/student")
    public ResponseEntity<MultiResponseDto<?>> getStudentSuggest(Pageable pageable) {

        Long memberId = 1L;
        Page<GetStudentSuggest> suggestions = suggestDbService.getStudentSuggestsOnMyPage(memberId, pageable);
        return ResponseEntity.ok(new MultiResponseDto<>(suggestions));
    }

    @DeleteMapping("/suggest/{suggest-id}")
    public ResponseEntity cancelSuggest(@PathVariable("suggest-id") Long suggestId) {

        Long memberId = 1L;
        suggestService.cancelSuggest(suggestId, memberId);
        return ResponseEntity.noContent().build();
    }

}
