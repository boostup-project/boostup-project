package com.codueon.boostUp.domain.suggest.toss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cancels {

    private String cancelReason;

    private Integer cancelAmount;

    private String canceledAt;

    private String transactionKey;

}
