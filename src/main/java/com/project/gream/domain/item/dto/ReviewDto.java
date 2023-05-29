package com.project.gream.domain.item.dto;


import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String content;
    private MemberDto memberDto;
    private ItemDto itemDto;

    @Builder
    public ReviewDto(Long id, int starValue, int priceScore, int qualityScore, int deliveryScore, int repurchaseScore, String content, MemberDto memberDto, ItemDto itemDto) {
        this.id = id;
        this.starValue = starValue;
        this.priceScore = priceScore;
        this.qualityScore = qualityScore;
        this.deliveryScore = deliveryScore;
        this.repurchaseScore = repurchaseScore;
        this.content = content;
        this.memberDto = memberDto;
        this.itemDto = itemDto;
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
                .memberDto(MemberDto.fromEntity(review.getMember()))
                .itemDto(ItemDto.fromEntity(review.getItem()))
                .build();
    }
}
