package com.codueon.boostUp.domain.suggest.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayCancelInfo {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partnerOrderId;
    private String partner_userId;
    private String paymentMethodType;
    private Amount amount;
    private ApprovedCancelAmount approvedCancelAmount;
    private CanceledAmount canceledAmount;
    private CancelAvailableAmount cancelAvailableAmount;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Date createdAt;
    private Date approvedAt;
    private Date canceledAt;
    private String orderStatus;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
