package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.PostType;
import com.project.gream.common.enumlist.QnaType;
import com.project.gream.common.enumlist.converter.PostTypeConverter;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
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
    private QnaType qnaType;
    private PostType postType;
    private ItemDto itemDto;
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
    public PostDto(Long id, String title, String content, int hits, String thumbnailUrl, QnaType qnaType, PostType postType,
                   ItemDto itemDto, MemberDto memberDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.thumbnailUrl = thumbnailUrl;
        this.qnaType = qnaType;
        this.postType = postType;
        this.itemDto = itemDto;
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
                .qnaType(qnaType)
                .postType(postType)
                .item(itemDto.toEntity())
                .member(memberDto.toEntity())
                .build();
    }

    public static PostDto fromEntity(Post post) {
        PostDto postDto =  PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hits(post.getHits())
                .thumbnailUrl(post.getThumbnailUrl())
                .qnaType(post.getQnaType())
                .postType(post.getPostType())
                .memberDto(MemberDto.fromEntity(post.getMember()))
                .createdTime(post.getCreatedTime())
                .modifiedTime(post.getModifiedTime())
                .build();

        if (post.getItem() == null) {
            return postDto;
        }
        postDto.setItemDto(ItemDto.fromEntity(post.getItem()));
        return postDto;
    }
}
