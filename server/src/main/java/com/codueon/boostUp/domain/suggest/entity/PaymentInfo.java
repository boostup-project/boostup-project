package com.codueon.boostUp.domain.suggest.entity;

import com.codueon.boostUp.domain.suggest.pay.ReadyToKakaoPaymentInfo;
import com.codueon.boostUp.domain.suggest.pay.ReadyToTossPaymentInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long id;

    private Integer quantity;

    /* ---------- 카카오 ---------- */

    private String cid;

    private String tid;

    private String partnerOrderId;

    private String partnerUserId;

    private String itemName;

    private Integer totalAmount;

    private Integer valAmount;

    private Integer taxFreeAmount;

    private String approvalUrl;

    private String cancelUrl;

    /* ---------- 공통 ---------- */

    private String failUrl;

    /* ---------- 토스 ---------- */

    private Integer amount;

    private String orderId;

    private String orderName;

    private String successUrl;

    /* -------------------- */

    @OneToOne
    @JoinColumn(name = "SUGGEST_ID")
    private Suggest suggest;

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    @Builder
    public PaymentInfo(Integer quantity) {
        this.quantity = quantity;
    }

    public void setKakaoPaymentInfo(ReadyToKakaoPaymentInfo params, String tid) {
        this.cid = params.getCid();
        this.tid = tid;
        this.partnerOrderId = params.getPartner_order_id();
        this.partnerUserId = params.getPartner_user_id();
        this.itemName = params.getItem_name();
        this.quantity = params.getQuantity();
        this.totalAmount = params.getTotal_amount();
        this.valAmount = params.getVal_amount();
        this.taxFreeAmount = params.getTax_free_amount();
        this.approvalUrl = params.getApproval_url();
        this.failUrl = params.getFail_url();
        this.cancelUrl = params.getCancel_url();
    }

    public void setTossPaymentInfo(ReadyToTossPaymentInfo body) {
        this.failUrl = body.getFailUrl();
        this.amount = body.getAmount();
        this.orderId = body.getOrderId();
        this.orderName = body.getOrderName();
        this.successUrl = body.getSuccessUrl();
    }
}
