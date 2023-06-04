package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.LikeTargetType;
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
public class LikesDto {

    private Long id;
    private Long targetId;
    private LikeTargetType likeTargetType;
    private MemberDto memberDto;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedTime;

    @Builder
    public LikesDto(Long id, Long targetId, LikeTargetType likeTargetType, MemberDto memberDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.targetId = targetId;
        this.likeTargetType = likeTargetType;
        this.memberDto = memberDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Likes toEntity() {
        return Likes.builder()
                .id(id)
                .targetId(targetId)
                .likeTargetType(likeTargetType)
                .member(memberDto.toEntity())
                .build();
    }

    public static LikesDto fromEntity(Likes likes) {
        return LikesDto.builder()
                .id(likes.getId())
                .targetId(likes.getTargetId())
                .likeTargetType(likes.getLikeTargetType())
                .memberDto(MemberDto.fromEntity(likes.member))
                .build();
    }
}
