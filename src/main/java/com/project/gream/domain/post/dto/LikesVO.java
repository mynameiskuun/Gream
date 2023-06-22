package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Likes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class LikesVO {

    private Long id;
    private MemberDto memberDto;
    private ItemDto itemDto;
    private ReviewDto reviewDto;
    private CommentDto commentDto;
    private PostDto postDto;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedTime;

    @Builder
    public LikesVO(Long id, MemberDto memberDto, ItemDto itemDto, ReviewDto reviewDto,
                   CommentDto commentDto, PostDto postDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.memberDto = memberDto;
        this.itemDto = itemDto;
        this.reviewDto = reviewDto;
        this.commentDto = commentDto;
        this.postDto = postDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Likes toEntity() {
        return Likes.builder()
                .id(id)
                .member(memberDto.toEntity())
                .item(itemDto.toEntity())
                .review(reviewDto.toEntity())
//                .comment(commentDto.toEntity())
//                .post(postDto.toEntity())
                .build();
    }

    public static LikesVO fromEntity(Likes likes) {
        return LikesVO.builder()
                .id(likes.getId())
                .memberDto(MemberDto.fromEntity(likes.member))
                .itemDto(ItemDto.fromEntity(likes.item))
                .reviewDto(ReviewDto.fromEntity(likes.review))
//                .commentDto(CommentDto.fromEntity(likes.comment))
//                .postDto(PostDto.fromEntity(likes.post))
                .build();
    }
}
