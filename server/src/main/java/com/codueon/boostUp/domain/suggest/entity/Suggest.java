package com.codueon.boostUp.domain.suggest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suggest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUGGEST_ID")
    private Long id;
    private String days;
    private String languages;

    @Column(length = 500)
    private String requests;

    @Enumerated(value = EnumType.STRING)
    private SuggestStatus suggestStatus;

    private Long lessonId;
    private Long memberId;
    private Integer totalCost;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String paymentMethod;

    @JsonBackReference
    @OneToOne(mappedBy = "suggest", cascade = CascadeType.REMOVE)
    private PaymentInfo paymentInfo;

    public void setStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public void setEndTime() {
        this.endTime = LocalDateTime.now();
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Builder
    public Suggest( Long id,
                    String days,
                    String languages,
                    String requests,
                    Long lessonId,
                    Long memberId,
                    Integer totalCost,
                    String paymentMethod) {
        this.id = id;
        this.days = days;
        this.languages = languages;
        this.requests = requests;
        this.lessonId = lessonId;
        this.memberId = memberId;
        this.totalCost = totalCost;
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(SuggestStatus status) {
        this.suggestStatus = status;
    }

    public void addPaymentInfo(PaymentInfo paymentInfo) {
        if (paymentInfo.getSuggest() != this) {
            paymentInfo.setSuggest(this);
        }
        this.paymentInfo = paymentInfo;
    }
}
