package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.common.enumlist.converter.LikeTargetTypeConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = true)
    public Item item;
    @ManyToOne
    @JoinColumn(nullable = true)
    public Review review;
    @ManyToOne
    @JoinColumn(nullable = true)
    public Comment comment;
    @ManyToOne
    @JoinColumn(nullable = true)
    public Post post;
    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member member;

    @Builder
    public Likes(Long id, Item item, Review review, Comment comment, Post post, Member member) {
        this.id = id;
        this.item = item;
        this.review = review;
        this.comment = comment;
        this.post = post;
        this.member = member;
    }
}
