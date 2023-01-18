package com.codueon.boostUp.domain.suggest.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForKakaoPayInfo {
    private String cid;
    private String tid;
    private String pg_token;
    private Integer total_amount;
    private String partner_user_id;
    private String partner_order_id;
}
