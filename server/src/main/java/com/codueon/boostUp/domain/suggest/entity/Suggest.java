package com.codueon.boostUp.domain.suggest.entity;

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
    private SuggestStatus status = SuggestStatus.ACCEPT_IN_PROGRESS;

    private Long lessonId;

    private Long memberId;

    private Integer quantity;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToOne(mappedBy = "suggest", cascade = CascadeType.REMOVE)
    private PaymentInfo paymentInfo;

    public void setStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public void setEndTime() {
        this.endTime = LocalDateTime.now();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Builder
    public Suggest(
            Long id,
            String days,
            String languages,
            String requests,
            Long lessonId,
            Long memberId) {
        this.id = id;
        this.days = days;
        this.languages = languages;
        this.requests = requests;
        this.lessonId = lessonId;
        this.memberId = memberId;
    }

    public void setStatus(SuggestStatus status) {
        this.status = status;
    }

    public enum SuggestStatus{
        ACCEPT_IN_PROGRESS(1, "수락 대기 중"),
        PAY_IN_PROGRESS(2, "결제 대기 중"), // 결제 취소, 결제 실패 포함
        DURING_LESSON(3, "과외 중"), // == 결제 완료
        END_OF_LESSON(4, "과외 종료");

        @Getter
        private int stepNumber;

        @Getter
        private String status;

        SuggestStatus(int stepNumber, String status) {
            this.stepNumber = stepNumber;
            this.status = status;
        }
    }
}
