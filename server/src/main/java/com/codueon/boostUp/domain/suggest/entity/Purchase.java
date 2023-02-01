package com.codueon.boostUp.domain.suggest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_ID")
    private Long id;
    private Long memberId;
    private Long lessonId;
    private Long ticketId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(value = EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @JsonBackReference
    @OneToOne(mappedBy = "purchase", cascade = CascadeType.REMOVE)
    private PaymentInfo paymentInfo;

    public void setPurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public void setStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public void setEndTime(LocalDateTime startTime, Integer validDays) {
        this.endTime = startTime.plusDays(validDays);
    }

    public void setTicketTimes(Integer validDays) {
        this.startTime = LocalDateTime.now();
        this.endTime = startTime.plusDays(validDays);
    }

    @Builder
    public Purchase(Long id, Long memberId, Long lessonId, Long ticketId) {
        this.id = id;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.ticketId = ticketId;
    }
}
