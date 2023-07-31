package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private int starValue;
    private int priceScore;
    private int qualityScore;
    private int deliveryScore;
    private int repurchaseScore;
    private Long likesCount;
    private String content;
    private String thumbnail;
    private MemberDto memberDto;
    private ItemDto itemDto;
    private Long orderItemId;
    private List<CommentDto.Response> commentList;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @Builder
    public ReviewDto(Long id, int starValue, int priceScore, int qualityScore, int deliveryScore, int repurchaseScore, Long likesCount,
                     String content, String thumbnail, MemberDto memberDto, ItemDto itemDto, Long orderItemId, List<CommentDto.Response> commentList) {
        this.id = id;
        this.starValue = starValue;
        this.priceScore = priceScore;
        this.qualityScore = qualityScore;
        this.deliveryScore = deliveryScore;
        this.repurchaseScore = repurchaseScore;
        this.likesCount = likesCount;
        this.content = content;
        this.thumbnail = thumbnail;
        this.memberDto = memberDto;
        this.itemDto = itemDto;
        this.orderItemId = orderItemId;
        this.commentList = commentList;
    }

    public Review toEntity() {
        return Review.builder()
                .id(id)
                .starValue(starValue)
                .priceScore(priceScore)
                .qualityScore(qualityScore)
                .deliveryScore(deliveryScore)
                .repurchaseScore(repurchaseScore)
                .content(content)
                .thumbnail(thumbnail)
                .member(memberDto.toEntity())
                .item(itemDto.toEntity())
                .build();
    }

    public static ReviewDto fromEntity(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .starValue(review.getStarValue())
                .priceScore(review.getPriceScore())
                .qualityScore(review.getQualityScore())
                .deliveryScore(review.getDeliveryScore())
                .repurchaseScore(review.getRepurchaseScore())
                .content(review.getContent())
                .thumbnail(review.getThumbnail())
                .memberDto(MemberDto.fromEntity(review.getMember()))
                .itemDto(ItemDto.fromEntity(review.getItem()))
                .build();
    }

}
