package com.codueon.boostUp.domain.suggest.entity;

import lombok.Getter;

@Getter
public enum PurchaseStatus {
    PURCHASE_IN_PROGRESS(1, "이용권 결제 대기 중"),
    TICKET_IN_USE(3, "이용권 사용 중"), // == 결제 완료
    END_OF_TICKET(4, "이용권 종료"),
    REFUND_TICKET(5, "환불 완료");

    private int stepNumber;
    private String status;

    PurchaseStatus(int stepNumber, String status) {
        this.stepNumber = stepNumber;
        this.status = status;
    }
}
