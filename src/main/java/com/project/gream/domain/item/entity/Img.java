package com.project.gream.domain.item.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.post.entity.Post;
import com.project.gream.domain.post.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Img extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;
    @Column(name = "img_url")
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", nullable = true)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    // 이미지는 리뷰의 이미지 일수도, 상품의 이미지 일수도 있기 때문에 nullable = true

    public Img(String imgUrl, Item item) {
        this.url = imgUrl;
        this.item = item;
    }

    @Builder
    public Img(Long id, String url, Item item, Review review, Post post) {
        this.id = id;
        this.url = url;
        this.item = item;
        this.review = review;
        this.post = post;
    }
}
