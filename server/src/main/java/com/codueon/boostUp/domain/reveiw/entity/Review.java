package com.codueon.boostUp.domain.reveiw.entity;

import com.codueon.boostUp.domain.reveiw.dto.PatchReview;
import com.codueon.boostUp.global.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW")
    private Long id;

    private Integer score;

    @Column(length = 1000)
    private String comment;

    private Long lessonId;

    private Long memberId;

    @Builder
    public Review(Long id, String comment, Integer score, Long lessonId, Long memberId) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.lessonId = lessonId;
        this.memberId = memberId;
    }

    public void editReview(PatchReview editReview) {
        Optional.ofNullable(editReview.getScore())
                .ifPresent(score -> this.score = score);
        Optional.ofNullable(editReview.getComment())
                .ifPresent(comment -> this.comment = comment);
    }
}
