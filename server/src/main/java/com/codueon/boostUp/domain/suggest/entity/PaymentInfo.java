package com.codueon.boostUp.domain.suggest.entity;

import com.codueon.boostUp.domain.suggest.pay.ReadyToPaymentInfo;
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

    private String cid;

    private String tid;

    private String partnerOrderId;

    private String partnerUserId;

    private String itemName;

    private Integer quantity;

    private Integer totalAmount;

    private Integer valAmount;

    private Integer taxFreeAmount;

    private String approvalUrl;

    private String failUrl;

    private String cancelUrl;

    @OneToOne
    @JoinColumn(name = "SUGGEST_ID")
    private Suggest suggest;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    @Builder
    public PaymentInfo(ReadyToPaymentInfo params, String tid) {
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
}
