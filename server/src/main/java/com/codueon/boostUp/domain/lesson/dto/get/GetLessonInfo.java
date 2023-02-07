package com.codueon.boostUp.domain.lesson.dto.get;

import com.codueon.boostUp.domain.lesson.dto.utils.CareerImageVO;
import com.codueon.boostUp.domain.lesson.entity.LessonInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetLessonInfo {
    private String introduction;
    private String detailCompany;
    private String personality;
    private String detailCost;
    private String detailLocation;
    private List<CareerImageVO> careerImages;
    @Builder
    public GetLessonInfo(LessonInfo lessonInfo) {
        this.introduction = lessonInfo.getIntroduction();
        this.detailCompany = lessonInfo.getCompanies();
        this.personality = lessonInfo.getPersonality();
        this.detailCost = lessonInfo.getCosts();
        this.detailLocation = lessonInfo.getFavoriteLocation();
        this.careerImages = lessonInfo.getCareerImages().stream()
                .map(careerImage -> CareerImageVO.builder()
                        .careerImageId(careerImage.getId())
                        .filePath(careerImage.getFilePath())
                        .build())
                .collect(Collectors.toList());
    }
}