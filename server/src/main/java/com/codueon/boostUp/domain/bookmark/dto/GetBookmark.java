package com.codueon.boostUp.domain.bookmark.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetBookmark {

    private Long bookmarkId;

    private Long lessonId;

    private String image;

    private String bookmarkUrl;

    private String title;

    private String name;

    private Integer cost;

    private List<Long> languages;

    private List<Long> address;

    @Builder
    public GetBookmark(Long bookmarkId,
                       Long lessonId,
                       String image,
                       String bookmarkUrl,
                       String title,
                       String name,
                       Integer cost,
                       List<Long> languages,
                       List<Long> address) {
        this.bookmarkId = bookmarkId;
        this.lessonId = lessonId;
        this.image = image;
        this.bookmarkUrl = bookmarkUrl;
        this.title = title;
        this.name = name;
        this.cost = cost;
        this.languages = languages;
        this.address = address;
    }
}
