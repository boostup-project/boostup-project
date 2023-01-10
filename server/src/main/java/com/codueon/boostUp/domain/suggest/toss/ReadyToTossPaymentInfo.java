package com.codueon.boostUp.domain.suggest.toss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadyToTossPaymentInfo {

    private Integer amount;

    private String failUrl;

    private String method;

    private String orderId;

    private String orderName;

    private String successUrl;

}
