package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReason {
    private String reason;

    @Builder
    public PostReason(String reason) {
        this.reason = reason;
    }
}
