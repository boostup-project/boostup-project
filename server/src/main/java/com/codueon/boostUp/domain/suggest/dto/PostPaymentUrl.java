package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class PostPaymentUrl {
    @NotNull(message = "횟수는 공백이 아니어야 합니다.")
    @Positive(message = "횟수는 1회 이상이어야 합니다.")
    private Integer quantity;

    @Builder
    public PostPaymentUrl(Integer quantity) {
        this.quantity = quantity;
    }
}
