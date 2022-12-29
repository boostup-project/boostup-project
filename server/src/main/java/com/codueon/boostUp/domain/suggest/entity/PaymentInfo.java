package com.codueon.boostUp.domain.suggest.entity;

import lombok.AccessLevel;
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
}
