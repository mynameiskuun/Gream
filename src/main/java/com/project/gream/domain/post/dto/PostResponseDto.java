package com.project.gream.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {

    private List<String> imgUrlList;
    private PostDto postDto;

    @Builder
    public PostResponseDto(List<String> imgUrlList, PostDto postDto) {
        this.imgUrlList = imgUrlList;
        this.postDto = postDto;
    }
}
