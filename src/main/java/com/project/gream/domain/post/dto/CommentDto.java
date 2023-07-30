package com.project.gream.domain.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.CommentTargetType;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private int depth;
    private MemberDto memberDto;
    private ReviewDto reviewDto;
    private PostDto postDto;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    @Builder
    public CommentDto(Long id, String content, int depth, MemberDto memberDto,
                      LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.content = content;
        this.depth = depth;
        this.memberDto = memberDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .depth(depth)
                .member(memberDto.toEntity())
                .build();
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .memberDto(MemberDto.fromEntity(comment.getMember()))
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private Long id;
        private String content;
        private String modifyContent;

        private MemberDto memberDto;
        private Long postId;
        private Long reviewId;

        @Builder
        public Request(Long id, String content, String modifyContent,
                       MemberDto memberDto, Long postId, Long reviewId) {
            this.id = id;
            this.content = content;
            this.modifyContent = modifyContent;
            this.memberDto = memberDto;
            this.postId = postId;
            this.reviewId = reviewId;
        }
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String content;
        private MemberDto memberDto;
        private Long likeCount;
        private int depth;
        private String message;
        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

        @Builder
        public Response(Long id, String content, MemberDto memberDto, Long likeCount, int depth,
                        String message, LocalDateTime createdTime, LocalDateTime modifiedTime) {
            this.id = id;
            this.content = content;
            this.memberDto = memberDto;
            this.likeCount = likeCount;
            this.depth = depth;
            this.message = message;
            this.createdTime = createdTime;
            this.modifiedTime = modifiedTime;
        }

        public Response(CommentDto commentDto) {
            this.id = commentDto.getId();
            this.content = commentDto.getContent();
            this.memberDto = commentDto.getMemberDto();
            this.depth = commentDto.getDepth();
            this.createdTime = commentDto.getCreatedTime();
            this.modifiedTime = commentDto.getModifiedTime();
        }
    }
}
