package com.codueon.boostUp.domain.suggest.dto;

import lombok.Getter;

@Getter
public class GetPaymentStatusCheck {
    private Boolean paymentCheck;

    public GetPaymentStatusCheck(Boolean paymentCheck) {
        this.paymentCheck = paymentCheck;
    }
}
