package com.project.gream.domain.item.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.entity.Img;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ImgDto {

    private Long id;
    private String url;
    private ItemVO itemVO;
    private ReviewDto reviewDto;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public ImgDto(Long id, String url, ItemVO itemVO, ReviewDto reviewDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.url = url;
        this.itemVO = itemVO;
        this.reviewDto = reviewDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Img toEntityForItem() {
        return Img.builder()
                .id(id)
                .url(url)
                .item(itemVO.toEntity())
                .build();
    }

    public Img toEntityForReview() {
        return Img.builder()
                .id(id)
                .url(url)
                .item(itemVO.toEntity())
                .review(reviewDto.toEntity())
                .build();
    }

    public static ImgDto fromEntityForItem(Img img) {
        return ImgDto.builder()
                .id(img.getId())
                .url(img.getUrl())
                .itemVO(ItemVO.fromEntity(img.getItem()))
                .build();
    }

    public static ImgDto fromEntityForReview(Img img) {
        return ImgDto.builder()
                .id(img.getId())
                .url(img.getUrl())
                .itemVO(ItemVO.fromEntity(img.getItem()))
                .reviewDto(ReviewDto.fromEntity(img.getReview()))
                .build();
    }
}
