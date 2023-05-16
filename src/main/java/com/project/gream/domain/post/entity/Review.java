package com.project.gream.domain.post.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private int starValue;
    private int priceScore;
    @Column(nullable = false)
    private int qualityScore;
    @Column(nullable = false)
    private int deliveryScore;
    @Column(nullable = false)
    private int repurchaseScore;
    @Column(nullable = false, name = "review_content")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Builder
    public Review(Long id, int starValue, int priceScore, int qualityScore, int deliveryScore, int repurchaseScore, String content, Member member, Item item) {
        this.id = id;
        this.starValue = starValue;
        this.priceScore = priceScore;
        this.qualityScore = qualityScore;
        this.deliveryScore = deliveryScore;
        this.repurchaseScore = repurchaseScore;
        this.content = content;
        this.member = member;
        this.item = item;
    }
}
