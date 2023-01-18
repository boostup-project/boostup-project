package com.codueon.boostUp.domain.suggest.toss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossCard {
    private Integer amount;
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private String installmentPlanMonths;
    private String approveNo;
    private String useCardPoint;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
    private String interestPayer;
    private Boolean isInterestFree;
}
