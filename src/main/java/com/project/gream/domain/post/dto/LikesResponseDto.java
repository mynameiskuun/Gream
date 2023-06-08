package com.project.gream.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LikesResponseDto {

    private long count;
    private String backgroundColor;
    private Long itemId;
    private List<Map<Long,Long>> reviewLikesMapList;
    private List<Map<Long,Long>> commentLikesMapList;

    public LikesResponseDto(long count, String backgroundColor) {
        this.count = count;
        this.backgroundColor = backgroundColor;
    }
}
