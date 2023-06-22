package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.CommentTargetType;
import com.project.gream.common.enumlist.converter.CommentTargetTypeConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
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

}
