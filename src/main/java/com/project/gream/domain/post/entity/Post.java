package com.project.gream.domain.post.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
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
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
