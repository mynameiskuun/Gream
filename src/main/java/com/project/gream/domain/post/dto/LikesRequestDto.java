package com.project.gream.domain.post.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LikesRequestDto {

    private Long itemId;
    private List<Long> reviewIdList;
    private List<Long> commentIdList;
    // 댓글 리스트
}
