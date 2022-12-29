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
    private Double average;
    private Integer totalReviews;
    private List<GetReview> data;
    private PageInfo pageInfo;

    public GetAllDetailReviews(Page<GetReview> pages) {
        List<GetReview> reviews = pages.getContent();
        this.totalReviews = reviews.size();
        double avg = reviews.stream().mapToInt(GetReview::getScore).sum() / totalReviews;
        this.average = Math.round(avg * 10) / 10.0;
        this.data = reviews;
        this.pageInfo = PageInfo.builder()
                .page(pages.getNumber() + 1)
                .size(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .build();
    }
}
