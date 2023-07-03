package com.project.gream.domain.post.dto;

import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private int hits;
    private MemberDto memberDto;

    @Builder
    public PostDto(Long id, String title, String content, int hits, MemberDto memberDto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.memberDto = memberDto;
    }

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .hits(hits)
                .member(memberDto.toEntity())
                .build();
    }

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hits(post.getHits())
                .memberDto(MemberDto.fromEntity(post.getMember()))
                .build();
    }
}
