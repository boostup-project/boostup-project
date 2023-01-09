package com.codueon.boostUp.domain.suggest.pay;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadyToTossPaymentInfo {

    private Integer amount;

    private String failUrl;

    private String method;

    private String orderId;

    private String orderName;

    private String successUrl;

    @Builder
    public ReadyToTossPaymentInfo(Integer amount,
                                  String failUrl,
                                  String method,
                                  String orderId,
                                  String orderName,
                                  String successUrl) {
        this.amount = amount;
        this.failUrl = failUrl;
        this.method = method;
        this.orderId = orderId;
        this.orderName = orderName;
        this.successUrl = successUrl;
    }
}
