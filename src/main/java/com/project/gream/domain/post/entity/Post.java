package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.PostType;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    private int hits;
    private PostType postType;
    private String thumbnailUrl;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(Long id, String title, String content, int hits, PostType postType, String thumbnailUrl, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.postType = postType;
        this.thumbnailUrl = thumbnailUrl;
        this.member = member;
    }
}
