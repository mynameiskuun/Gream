package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LikeTargetType {

    POST("게시판"),
    REVIEW("리뷰"),
    ITEM("상품"),
    COMMENT("댓글");

    private final String value;
    public String getValue() {
        return this.value;
    }

}
