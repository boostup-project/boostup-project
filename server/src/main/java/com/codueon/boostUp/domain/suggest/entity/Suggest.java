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
}
