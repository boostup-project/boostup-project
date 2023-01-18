package com.codueon.boostUp.domain.suggest.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForKakaoPayCancelInfo {
    private String cid;
    private String tid;
    private Integer cancel_amount;
    private Integer cancel_tax_free_amount;
}
