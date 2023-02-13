package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.domain.lesson.dto.post.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.patch.PostLessonDetailEdit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LessonInfo {

    @Id
    @Column(name = "LESSON_INFO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lessonId;
    private String introduction;
    private String companies;
    private String favoriteLocation;
    private String personality;
    private String costs;
    private Long memberId;

    @JsonManagedReference
    @OneToMany(mappedBy = "lessonInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerImage> careerImages = new ArrayList<>();

    @Builder
    public LessonInfo(Long id,
                      Long lessonId,
                      String introduction,
                      String companies,
                      String favoriteLocation,
                      String personality,
                      String costs,
                      Long memberId) {
        this.id = id;
        this.lessonId = lessonId;
        this.introduction = introduction;
        this.companies = companies;
        this.favoriteLocation = favoriteLocation;
        this.personality = personality;
        this.costs = costs;
        this.memberId = memberId;
    }

    public void addCareerImage(CareerImage careerImage) {
        if(careerImage.getLessonInfo() != this) careerImage.addLessonInfo(this);
        careerImages.add(careerImage);
    }

    public static LessonInfo toEntity(Long lessonId, PostLesson postLesson, Long memberId) {
        return LessonInfo.builder()
                .lessonId(lessonId)
                .introduction(postLesson.getIntroduction())
                .companies(postLesson.getDetailCompany())
                .favoriteLocation(postLesson.getDetailLocation())
                .personality(postLesson.getPersonality())
                .costs(postLesson.getDetailCost())
                .memberId(memberId)
                .build();
    }

    public void editLessonDetail(PostLessonDetailEdit postLessonDetailEdit) {
        this.introduction = postLessonDetailEdit.getIntroduction();
        this.companies = postLessonDetailEdit.getDetailCompany();
        this.costs = postLessonDetailEdit.getDetailCost();
        this.personality = postLessonDetailEdit.getPersonality();
        this.favoriteLocation = postLessonDetailEdit.getDetailLocation();
    }

    public void editCareerImage(List<CareerImage> careerImages) {
        this.careerImages.clear();
        this.careerImages.addAll(careerImages);
    }
}
