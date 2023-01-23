package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.domain.lesson.dto.PostLesson;
import com.codueon.boostUp.domain.lesson.dto.PostLessonDetailEdit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                      String costs) {
        this.id = id;
        this.lessonId = lessonId;
        this.introduction = introduction;
        this.companies = companies;
        this.favoriteLocation = favoriteLocation;
        this.personality = personality;
        this.costs = costs;
    }

    public void addCareerImage(CareerImage careerImage) {
        if(careerImage.getLessonInfo() != this) careerImage.addLessonInfo(this);
        careerImages.add(careerImage);
    }

    public static LessonInfo toEntity(Long lessonId, PostLesson postLesson) {
        return LessonInfo.builder()
                .lessonId(lessonId)
                .introduction(postLesson.getIntroduction())
                .companies(postLesson.getDetailCompany())
                .favoriteLocation(postLesson.getDetailLocation())
                .personality(postLesson.getPersonality())
                .costs(postLesson.getDetailCost())
                .build();
    }

    public void editLessonDetail(PostLessonDetailEdit postLessonDetailEdit) {
        this.introduction = postLessonDetailEdit.getIntroduction();
        this.companies = postLessonDetailEdit.getDetailCompany();
        this.costs = postLessonDetailEdit.getDetailCost();
        this.personality = postLessonDetailEdit.getPersonality();
        this.favoriteLocation = postLessonDetailEdit.getDetailLocation();
        this.careerImages.clear();
    }
}
