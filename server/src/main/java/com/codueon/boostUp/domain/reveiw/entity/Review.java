package com.codueon.boostUp.domain.reveiw.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW")
    private Long id;

    private String title;

    @Column(length = 1000)
    private String comment;

    private Long lessonId;

    private Long memberId;

    @Builder
    public Review(Long id, String title, String comment, Long lessonId, Long memberId) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.lessonId = lessonId;
        this.memberId = memberId;
    }
}
