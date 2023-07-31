package com.project.gream.domain.post.dto;

import com.project.gream.domain.item.dto.ItemDto;
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
    private String message;
    private ItemDto itemDto;
    private Long reviewId;

    @Builder
    public PostResponseDto(List<String> imgUrlList, PostDto postDto, String message, ItemDto itemDto, Long reviewId) {
        this.imgUrlList = imgUrlList;
        this.postDto = postDto;
        this.message = message;
        this.itemDto = itemDto;
        this.reviewId = reviewId;
    }
    public PostResponseDto(List<String> imgUrlList, PostDto postDto) {
        this.imgUrlList = imgUrlList;
        this.postDto = postDto;
    }
}
