package com.project.gream.domain.post.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LikesResponseDto {

    private long count;
    private String backgroundColor;
    private Long itemLikesCount;
    private Map<Long, Long> reviewLikesMap;
    private Map<String, String> itemLikeBackgroundColorMap;
    private Map<Long, Long> reviewLikeCountsMap;
    private Map<String, String> reviewLikeBackgroundColorMap;
    private Map<Long,Long> commentLikesMapList;

    @Builder
    public LikesResponseDto(long count, String backgroundColor, Long itemLikesCount, Map<Long, Long> reviewLikesMap, Map<String, String> itemLikeBackgroundColorMap,
                            Map<Long, Long> reviewLikeCountsMap, Map<String, String> reviewLikeBackgroundColorMap, Map<Long, Long> commentLikesMapList) {
        this.count = count;
        this.backgroundColor = backgroundColor;
        this.itemLikesCount = itemLikesCount;
        this.reviewLikesMap = reviewLikesMap;
        this.itemLikeBackgroundColorMap = itemLikeBackgroundColorMap;
        this.reviewLikeCountsMap = reviewLikeCountsMap;
        this.reviewLikeBackgroundColorMap = reviewLikeBackgroundColorMap;
        this.commentLikesMapList = commentLikesMapList;
    }

    public LikesResponseDto(long count, String backgroundColor) {
        this.count = count;
        this.backgroundColor = backgroundColor;
    }

}
