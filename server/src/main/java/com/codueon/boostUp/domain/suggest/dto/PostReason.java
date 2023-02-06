package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostReason {
    @NotBlank(message = "사유는 공백이 아니어야 합니다.")
    private String reason;

    @Builder
    public PostReason(String reason) {
        this.reason = reason;
    }
}
