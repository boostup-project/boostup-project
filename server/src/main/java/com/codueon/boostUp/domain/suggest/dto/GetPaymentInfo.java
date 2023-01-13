package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetPaymentInfo {

    private String title;

    private String name;

    private List<String> languages;

    private List<String> address;

    private String company;

    private String profileImage;

    private Integer cost;

    private Integer totalCost;

    private Integer quantity;


    @Builder
    public GetPaymentInfo(Lesson lesson,
                          Integer totalCost,
                          Integer quantity ) {

        this.title = lesson.getTitle();
        this.name = lesson.getName();
        this.languages = lesson.getLessonLanguages().stream()
                .map(language -> language.getLanguages().getLanguages())
                .collect(Collectors.toList());
        this.address = lesson.getLessonAddresses().stream()
                .map(address -> address.getAddress().getAddress())
                .collect(Collectors.toList());
        this.company = lesson.getCompany();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.cost = lesson.getCost();
        this.totalCost = totalCost;
        this.quantity = quantity;

    }
}
