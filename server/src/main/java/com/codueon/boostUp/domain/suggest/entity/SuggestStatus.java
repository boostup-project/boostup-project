package com.codueon.boostUp.domain.suggest.entity;

import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SuggestStatus {
    ACCEPT_IN_PROGRESS(1, "수락 대기 중"),
    PAY_IN_PROGRESS(2, "결제 대기 중"), // 결제 취소, 결제 실패 포함
    DURING_LESSON(3, "과외 중"), // == 결제 완료
    END_OF_LESSON(4, "과외 종료"),
    REFUND_PAYMENT(5, "환불 완료");

    private int stepNumber;
    private String status;

    SuggestStatus(int stepNumber, String status) {
        this.stepNumber = stepNumber;
        this.status = status;
    }

    private static final Map<String, SuggestStatus> descriptions =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(SuggestStatus::getStatus, Function.identity())));

    public static SuggestStatus findByStatus(String status) {
        return Optional.ofNullable(descriptions.get(status))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STATUS_NOT_FOUND));
    }
}
