package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentTargetType {

    POST("게시판"),
    REVIEW("리뷰");

    private final String value;
    public String getValue() {
        return this.value;
    }

}
