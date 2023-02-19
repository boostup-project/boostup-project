package com.codueon.boostUp.domain.reveiw.dto;

import com.codueon.boostUp.domain.dto.PageInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllDetailReviews {
    private double average;
    private Integer totalReviews;
    private List<GetReview> data;
    private PageInfo pageInfo;

    public GetAllDetailReviews(Page<GetReview> pages) {
        List<GetReview> reviews = pages.getContent();
        this.totalReviews = reviews.size();
        double avg = reviews.stream().mapToInt(GetReview::getScore).sum() / (double)((totalReviews == 0) ? 1 : totalReviews);
        this.average = Math.round(avg * 100) / 100.0;
        this.data = reviews;
        this.pageInfo = PageInfo.builder()
                .page(pages.getNumber() + 1)
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .build();
    }
}
