package com.codueon.boostUp.domain.suggest.dto;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetRefundPayment {
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
    private Integer quantityCount;
    private Integer cancelCost;

    @Builder
    public GetRefundPayment(Lesson lesson,
                            String name,
                            Integer totalCost,
                            String paymentMethod,
                            Integer quantity,
                            Integer quantityCount) {
        this.title = lesson.getTitle();
        this.name = name;
        this.languages = lesson.getLanguageListAsString();
        this.address = lesson.getAddressListAsString();
        this.company = lesson.getCompany();
        this.profileImage = lesson.getProfileImage().getFilePath();
        this.cost = lesson.getCost();
        this.totalCost = totalCost;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.quantityCount = quantityCount;
        this.cancelCost = (quantity - quantityCount) * lesson.getCost();
    }
}
