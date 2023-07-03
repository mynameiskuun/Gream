package com.project.gream.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {

    private Long id;
    private String period;
    private String target;
    private String searchKeyWords;

    @Builder
    public PostRequestDto(Long id, String period, String target, String searchKeyWords) {
        this.id = id;
        this.period = period;
        this.target = target;
        this.searchKeyWords = searchKeyWords;
    }
}
