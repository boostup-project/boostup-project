package com.codueon.boostUp.domain.bookmark.dto;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.entity.ProfileImage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

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

    private List<String> languages;

    private List<String> address;

    @Builder
    @QueryProjection
    public GetBookmark(Lesson lesson,
                       Long bookmarkId,
                       String bookmarkUrl,
                       String name) {
        this.bookmarkId = bookmarkId;
        this.lessonId = lesson.getId();
        this.image = lesson.getProfileImage().getFilePath();
        this.bookmarkUrl = bookmarkUrl;
        this.title = lesson.getTitle();
        this.name = name;
        this.cost = lesson.getCost();
        this.languages = lesson.getLanguageListAsString();
        this.address = lesson.getAddressListAsString();
    }
}
