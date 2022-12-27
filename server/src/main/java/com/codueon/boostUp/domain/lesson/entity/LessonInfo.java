package com.codueon.boostUp.domain.lesson.entity;

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
    private String introduction;
    private String companies;
    private String favoriteLocation;
    private String personality;
    private String costs;
    @OneToMany(mappedBy = "lessonInfo", cascade = CascadeType.ALL)
    private List<CareerImage> careerImages = new ArrayList<>();

    @Builder
    public LessonInfo(Long id,
                      String introduction,
                      String companies,
                      String favoriteLocation,
                      String personality,
                      String costs) {
        this.id = id;
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
}
