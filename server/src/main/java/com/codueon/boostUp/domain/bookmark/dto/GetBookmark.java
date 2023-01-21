package com.codueon.boostUp.domain.bookmark.dto;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    private List<String> languages;

    private List<String> address;

    @Builder
    @QueryProjection
    public GetBookmark(Bookmark bookmark, Lesson lesson) {
        this.bookmarkId = bookmark.getId();
        this.lessonId = lesson.getId();
        this.image = lesson.getProfileImage().getFilePath();
        this.bookmarkUrl = bookmark.getBookmarkUrl();
        this.title = lesson.getTitle();
        this.name = lesson.getName();
        this.cost = lesson.getCost();
        this.languages = lesson.getLanguageListAsString();
        this.address = lesson.getAddressListAsString();
    }
}
