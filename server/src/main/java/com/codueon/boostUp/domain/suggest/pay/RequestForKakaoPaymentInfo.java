package com.codueon.boostUp.domain.suggest.pay;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestForKakaoPaymentInfo {

    private String cid;
    private String tid;
    private String pg_token;
    private Integer total_amount;
    private String partner_user_id;
    private String partner_order_id;

    @Builder
    public RequestForKakaoPaymentInfo(String cid,
                                      String tid,
                                      String pg_token,
                                      Integer total_amount,
                                      String partner_user_id,
                                      String partner_order_id) {
        this.cid = cid;
        this.tid = tid;
        this.pg_token = pg_token;
        this.total_amount = total_amount;
        this.partner_user_id = partner_user_id;
        this.partner_order_id = partner_order_id;
    }
}
