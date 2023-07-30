package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.CommentTargetType;
import com.project.gream.common.enumlist.converter.CommentTargetTypeConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import com.querydsl.core.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int depth;
    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member member;
    @ManyToOne()
    @JoinColumn(name = "post_id")
    public Post post;
    @ManyToOne
    @JoinColumn(name = "review_id")
    public Review review;

    @Builder
    public Comment(Long id, String content, int depth,
                   Member member, Post post, Review review) {
        this.id = id;
        this.content = content;
        this.depth = depth;
        this.member = member;
        this.post = post;
        this.review = review;
    }

    public void updateContent(String content) {
        if (StringUtils.isNullOrEmpty(content)) {
            return;
        }
        this.content = content;
    }
}
