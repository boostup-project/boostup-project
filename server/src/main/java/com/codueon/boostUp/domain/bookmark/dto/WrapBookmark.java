package com.codueon.boostUp.domain.bookmark.dto;

import lombok.Getter;

@Getter
public class WrapBookmark {
    private boolean bookmark;

    public WrapBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
