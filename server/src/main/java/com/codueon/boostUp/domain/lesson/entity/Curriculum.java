package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.domain.lesson.dto.patch.PatchLessonCurriculum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum {

    @Id
    @Column(name = "CURRICULUM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 5000)
    private String curriculum;
    private Long lessonId;
    private Long memberId;

    @Builder
    public Curriculum(Long id,
                      String curriculum,
                      Long lessonId,
                      Long memberId) {
        this.id = id;
        this.curriculum = curriculum;
        this.lessonId = lessonId;
        this.memberId = memberId;
    }

    public void editCurriculum(PatchLessonCurriculum patchLessonCurriculum) {
        this.curriculum = patchLessonCurriculum.getCurriculum();
    }
}
