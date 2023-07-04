package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private int hits;
    private String thumbnailUrl;
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
    public PostDto(Long id, String title, String content, int hits, String thumbnailUrl, MemberDto memberDto,
                   LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.thumbnailUrl = thumbnailUrl;
        this.memberDto = memberDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .hits(hits)
                .thumbnailUrl(thumbnailUrl)
                .member(memberDto.toEntity())
                .build();
    }

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hits(post.getHits())
                .thumbnailUrl(post.getThumbnailUrl())
                .memberDto(MemberDto.fromEntity(post.getMember()))
                .createdTime(post.getCreatedTime())
                .modifiedTime(post.getModifiedTime())
                .build();
    }
}
