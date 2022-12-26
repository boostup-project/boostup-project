package com.codueon.boostUp.domain.suggest.contoller;

import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SuggestController {

    @PostMapping("/lesson/{lesson-id}/suggest")
    public ResponseEntity<?> createSuggest(@PathVariable("lesson-id") Long lessonId,
                                      @RequestBody @Valid PostSuggest post,
                                      Long memberId) {

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/lesson/{lesson-id}/suggest/{suggest-id}/accept")
    public ResponseEntity acceptSuggest(@PathVariable("lesson-id") Long lessonId,
                                        @PathVariable("suggest-id") Long suggestId) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lesson/{lesson-id}/suggest")
    public ResponseEntity declineSuggest(@PathVariable("lesson-id") Long lessonId) {

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lesson/suggest/{suggest-id}/payment")
    public ResponseEntity  orderPayment(@PathVariable("suggest-id") Long suggestId) {

        return ResponseEntity.ok().build();
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

    @GetMapping("/suggest/teacher")
    public ResponseEntity getTeacherSuggest() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/suggest/student")
    public ResponseEntity getStudentSuggest() {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/suggest/{suggest-id}")
    public ResponseEntity cancelSuggest(@PathVariable("lesson-id") Long lessonId) {

        return ResponseEntity.noContent().build();
    }

}
