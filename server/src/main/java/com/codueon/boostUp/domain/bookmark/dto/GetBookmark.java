package com.codueon.boostUp.domain.bookmark.dto;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
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
    @QueryProjection
    public GetBookmark(Bookmark bookmark, Lesson lesson) {
        this.bookmarkId = bookmark.getId();
        this.lessonId = lesson.getId();
        this.image = "";
        this.bookmarkUrl = bookmarkUrl;
        this.title = title;
        this.name = name;
        this.cost = cost;
        this.languages = languages;
        this.address = address;
    }
}
