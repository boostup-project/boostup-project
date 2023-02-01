package com.codueon.boostUp.domain.suggest.dto;

import lombok.Getter;

@Getter
public class WrapPaymentStatusCheck {
    private Boolean paymentCheck;

    public WrapPaymentStatusCheck(Boolean paymentCheck) {
        this.paymentCheck = paymentCheck;
    }
}
