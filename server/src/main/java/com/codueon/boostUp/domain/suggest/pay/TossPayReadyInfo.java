package com.codueon.boostUp.domain.suggest.pay;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPayReadyInfo {

    private Integer totalAmount;

    private String paymentKey;

    private String method;

    private String orderId;

    private String orderName;

    private Checkout checkout;

    private String mId;

    private String requestedAt;

    public TossPayReadyInfo(Integer totalAmount,
                            String paymentKey,
                            String method,
                            String orderId,
                            String orderName,
                            Checkout checkout,
                            String mId,
                            String requestedAt) {
        this.totalAmount = totalAmount;
        this.paymentKey = paymentKey;
        this.method = method;
        this.orderId = orderId;
        this.orderName = orderName;
        this.checkout = checkout;
        this.mId = mId;
        this.requestedAt = requestedAt;
    }
}
