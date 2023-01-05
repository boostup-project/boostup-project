package com.codueon.boostUp.domain.suggest.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REASON_ID")
    private Long id;

    private String reason;

    private Long suggestId;

    private Long lessonId;

    @Builder
    public Reason(Long id, String reason, Long suggestId, Long lessonId) {
        this.id = id;
        this.reason = reason;
        this.suggestId = suggestId;
        this.lessonId = lessonId;
    }
}
