package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetPaymentReceipt {
    private String paymentMethod;
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
    public GetPaymentReceipt(Lesson lesson,
                             String tutorName,
                             Integer totalCost,
                             Integer quantity,
                             String paymentMethod ) {
        this.title = lesson.getTitle();
        this.name = tutorName;
        this.languages = lesson.getLanguageListAsString();
        this.address = lesson.getAddressListAsString();
        this.company = lesson.getCompany();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.cost = lesson.getCost();
        this.totalCost = totalCost;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
    }
}
