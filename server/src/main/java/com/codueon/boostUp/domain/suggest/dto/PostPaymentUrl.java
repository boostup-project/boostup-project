package com.codueon.boostUp.domain.suggest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPaymentUrl {
    private Integer quantity;

    @Builder
    public PostPaymentUrl(Integer quantity) {
        this.quantity = quantity;
    }
}
