package com.codueon.boostUp.domain.bookmark.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @Column(length = 1000)
    private String bookmarkUrl;

    private Long memberId;

    private Long lessonId;

    @Builder
    public Bookmark(Long id,
                    Long memberId,
                    Long lessonId) {
        this.id = id;
        this.bookmarkUrl = "http://localhost:300/lesson/" + lessonId;
        this.memberId = memberId;
        this.lessonId = lessonId;
    }
}
