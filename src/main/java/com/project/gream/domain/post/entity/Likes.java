package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.common.enumlist.converter.LikeTargetTypeConverter;
import com.project.gream.common.util.BaseTimeEntity;
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
    @Column(nullable = false)
    private Long targetId;
    @Convert(converter = LikeTargetTypeConverter.class)
    private LikeTargetType likeTargetType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member member;

    @Builder
    public Likes(Long id, Long targetId, LikeTargetType likeTargetType, Member member) {
        this.id = id;
        this.targetId = targetId;
        this.likeTargetType = likeTargetType;
        this.member = member;
    }
}
