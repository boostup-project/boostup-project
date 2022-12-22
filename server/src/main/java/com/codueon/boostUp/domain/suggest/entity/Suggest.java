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

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String language;

    @Column(length = 500)
    private String requests;

    @Enumerated(value = EnumType.STRING)
    private SuggestStatus status = SuggestStatus.ACCEPT_IN_PROGRESS;

    private Long lessonId;

    private Long memberId;

    @OneToOne(mappedBy = "suggest", cascade = CascadeType.REMOVE)
    private PaymentInfo paymentInfo;

    @Builder
    public Suggest(
            Long id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String language,
            String requests,
            Long lessonId,
            Long memberId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.requests = requests;
        this.lessonId = lessonId;
        this.memberId = memberId;
    }

    public enum SuggestStatus{
        ACCEPT_IN_PROGRESS(1, "수락 대기중"),
        PAY_IN_PROGRESS(2, "결제 대기중"),
        ACCEPT_DENIED(3, "수락 거절"),
        PAY_SUCCESS(4, "결제 성공"),
        PAY_FAILED(5, "결제 실패"),
        PAY_CANCELED(6, "결제 취소"),
        SUGGEST_CANCEL(7, "신청 취소"),
        PENDING_REFUND(8, "환불 요청"),
        ACCEPT_REFUND(9, "환불 수락"),
        REFUND_DENIED(10, "환불 거절"),
        END_OF_LESSON(11, "과외 종료");

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
