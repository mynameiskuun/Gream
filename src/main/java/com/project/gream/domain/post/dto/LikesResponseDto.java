package com.project.gream.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LikesResponseDto {

    private long count;
    private String backgroundColor;

    public LikesResponseDto(long count, String backgroundColor) {
        this.count = count;
        this.backgroundColor = backgroundColor;
    }
}
