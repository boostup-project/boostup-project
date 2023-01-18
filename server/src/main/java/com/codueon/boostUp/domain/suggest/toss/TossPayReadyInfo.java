package com.codueon.boostUp.domain.suggest.toss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossPayReadyInfo {
    private Integer totalAmount;
    private String paymentKey;
    private String method;
    private String orderId;
    private String orderName;
    private Checkout checkout;
    private String mId;
    private String requestedAt;
}
